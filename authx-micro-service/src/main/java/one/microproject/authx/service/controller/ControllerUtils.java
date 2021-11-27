package one.microproject.authx.service.controller;

import one.microproject.authx.common.dto.ClientCredentials;

import javax.servlet.http.HttpServletRequest;
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

}
