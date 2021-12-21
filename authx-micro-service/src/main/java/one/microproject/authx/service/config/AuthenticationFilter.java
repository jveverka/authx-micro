package one.microproject.authx.service.config;

import one.microproject.authx.jredis.TokenCacheReaderService;
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

    private static final Set<String> PUBLIC_RESOURCES = Set.of("/api/v1/system/**", "/api/v1/oauth2/**");
    private static final String GLOBAL_ADMIN_RESOURCES = "/api/v1/admin/authx/**";
    private static final String PROJECT_ADMIN_RESOURCES = "/api/v1/admin/project/**";

    private final TokenCacheReaderService tokenCacheReaderService;

    @Autowired
    public AuthenticationFilter(TokenCacheReaderService tokenCacheReaderService) {
        this.tokenCacheReaderService = tokenCacheReaderService;
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
        //tokenCacheReaderService.verify();
        AntPathMatcher matcher = new AntPathMatcher();
        if (matcher.match(GLOBAL_ADMIN_RESOURCES, request.getServletPath())) {

        } else if (matcher.match(PROJECT_ADMIN_RESOURCES, request.getServletPath())) {

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
