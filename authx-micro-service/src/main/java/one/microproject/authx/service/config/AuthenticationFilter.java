package one.microproject.authx.service.config;

import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.TokenContext;
import one.microproject.authx.jredis.TokenCacheReaderService;
import one.microproject.authx.service.service.AdminAuthXService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static one.microproject.authx.common.Constants.AUTHORIZATION;
import static one.microproject.authx.common.Constants.BEARER_PREFIX;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    private static final Set<String> PUBLIC_RESOURCES = Set.of("/api/v1/system/**", "/api/v1/oauth2/**", "/swagger-ui/**", "/v3/api-docs/**");
    private static final String GLOBAL_ADMIN_RESOURCES = "/api/v1/admin/authx/**";
    private static final String PROJECT_ADMIN_RESOURCES = "/api/v1/admin/project/**";

    private final TokenCacheReaderService tokenCacheReaderService;
    private final AdminAuthXService adminAuthXService;

    @Autowired
    public AuthenticationFilter(TokenCacheReaderService tokenCacheReaderService, AdminAuthXService adminAuthXService) {
        this.tokenCacheReaderService = tokenCacheReaderService;
        this.adminAuthXService = adminAuthXService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOGGER.debug("doFilterInternal {}{}", request.getContextPath(), request.getServletPath());
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        if (!authHeader.startsWith(BEARER_PREFIX)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        String token = authHeader.substring(BEARER_PREFIX.length());
        Optional<TokenContext> tokenContext = tokenCacheReaderService.verify(token);
        if (tokenContext.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        TokenClaims tokenClaims = tokenContext.get().tokenClaims();
        AntPathMatcher matcher = new AntPathMatcher();
        boolean isGlobalAdminProject = adminAuthXService.isGlobalAdminProject(tokenClaims.projectId());
        boolean isAdminUser = adminAuthXService.isAdminUser(tokenClaims.projectId(), tokenClaims.subject());
        if (matcher.match(GLOBAL_ADMIN_RESOURCES, request.getServletPath())) {
            if (isGlobalAdminProject && isAdminUser) {
                request.setAttribute(TokenClaims.class.getCanonicalName(), tokenClaims);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        } else if (matcher.match(PROJECT_ADMIN_RESOURCES, request.getServletPath())) {
            if (isAdminUser) {
                request.setAttribute(TokenClaims.class.getCanonicalName(), tokenClaims);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher matcher = new AntPathMatcher();
        Optional<String> f = PUBLIC_RESOURCES.stream().filter(r -> matcher.match(r, request.getServletPath())).findAny();
        LOGGER.debug("shouldNotFilter: {} {}", request.getServletPath(), f.isPresent());
        return f.isPresent();
    }

}
