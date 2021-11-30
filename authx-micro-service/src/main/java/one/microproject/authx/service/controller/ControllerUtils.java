package one.microproject.authx.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.service.service.impl.UrlMapper;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static one.microproject.authx.common.Constants.AUTHORIZATION;

public final class ControllerUtils {

    private ControllerUtils() {
    }

    public static Optional<ClientCredentials> getClientCredentials(HttpServletRequest request, String clientId, String clientSecret) {
        if (clientId != null && clientSecret != null) {
            return Optional.of(new ClientCredentials(clientId, clientSecret));
        }
        String authorization = request.getHeader(AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Basic ")) {
            String[] authorizations = authorization.split(" ");
            byte[] decoded = Base64.getDecoder().decode(authorizations[1]);
            String decodedString = new String(decoded);
            String[] usernamePassword = decodedString.split(":");
            return Optional.of(new ClientCredentials(usernamePassword[0], usernamePassword[1]));
        }
        return Optional.empty();
    }

    public static Set<String> getScopes(String scope) {
        if (scope == null) {
            return Set.of();
        } else {
            Set<String> scopes = new HashSet<>();
            String[] rawScopes = scope.trim().split(" ");
            for (String s: rawScopes) {
                if (!s.isEmpty()) {
                    scopes.add(s);
                }
            }
            return scopes;
        }
    }

    public static String getContextPath(String servletContextPath) {
        if (servletContextPath == null || servletContextPath.isEmpty())  {
            return "";
        } else if ("/".equals(servletContextPath)) {
            return "";
        } else if (!servletContextPath.isEmpty() && !servletContextPath.startsWith("/")) {
            return "/" + servletContextPath;
        } else {
            return servletContextPath;
        }
    }

    public static URI getIssuerUri(String servletContextPath, URL requestUrl, String projectId, UrlMapper urlMapper) throws URISyntaxException {
        String contextPath = getContextPath(servletContextPath);
        String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + ":" + requestUrl.getPort() + contextPath;
        baseUrl = urlMapper.map(baseUrl);
        return new URI(baseUrl + "/api/v1/oauth2/" + projectId );
    }

}
