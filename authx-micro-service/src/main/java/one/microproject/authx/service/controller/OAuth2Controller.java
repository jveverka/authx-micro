package one.microproject.authx.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.GrantType;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.JWKResponse;
import one.microproject.authx.common.dto.oauth2.ProviderConfigurationResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.common.dto.oauth2.UserInfoResponse;
import one.microproject.authx.service.exceptions.OAuth2TokenException;
import one.microproject.authx.service.service.OAuth2Service;
import one.microproject.authx.service.service.impl.UrlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;

import static one.microproject.authx.common.Constants.BEARER_PREFIX;
import static one.microproject.authx.service.controller.ControllerUtils.getClientCredentials;
import static one.microproject.authx.service.controller.ControllerUtils.getIssuerUri;
import static one.microproject.authx.service.controller.ControllerUtils.getScopes;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(path = "/api/v1/oauth2")
@Tag(name = "OAuth2", description = "APIs providing OAuth2 authentication flows.")
public class OAuth2Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Controller.class);

    private final ServletContext servletContext;
    private final UrlMapper urlMapper;
    private final OAuth2Service oAuth2Service;

    @Autowired
    public OAuth2Controller(ServletContext servletContext,
                            OAuth2Service oAuth2Service,
                            UrlMapper urlMapper) {
        this.servletContext = servletContext;
        this.urlMapper = urlMapper;
        this.oAuth2Service = oAuth2Service;
    }

    @Operation(description =
            "This endpoint represents the end of all authorizations flows when if successful, tokens are issued.\n" +
                    "Get Access Tokens for authorizations flows: \n" +
                    "- [grant_type=refresh_token](https://tools.ietf.org/html/rfc6749#section-2.3.1) \n" +
                    "- [grant_type=authorization_code](https://tools.ietf.org/html/rfc6749#section-4.1.3) \n" +
                    "- [grant_type=password](https://tools.ietf.org/html/rfc6749#section-4.3.2) \n" +
                    "- [grant_type=client_credentials](https://tools.ietf.org/html/rfc6749#section-4.4.2) \n" +
                    "- [grant_type=refresh_token](https://tools.ietf.org/html/rfc6749#section-6) \n",
            parameters = {
                    @Parameter(name = "project-id", description = "Unique project identifier.", in = ParameterIn.PATH, required = true),
                    @Parameter(name = "grant_type", description = "Grant type.", in = ParameterIn.QUERY, required = true),
                    @Parameter(name = "username", description = "User name.", in = ParameterIn.QUERY, required = false),
                    @Parameter(name = "password", description = "Password.", in = ParameterIn.QUERY, required = false),
                    @Parameter(name = "scope", description = "Scope.", in = ParameterIn.QUERY, required = false),
                    @Parameter(name = "client_id", description = "Client Id.", in = ParameterIn.QUERY, required = false),
                    @Parameter(name = "client_secret", description = "Client secret.", in = ParameterIn.QUERY, required = false),
                    @Parameter(name = "refresh_token", description = "Refresh token.", in = ParameterIn.QUERY, required = false),
                    @Parameter(name = "code", description = "Code.", in = ParameterIn.QUERY, required = false),
                    @Parameter(name = "nonce", description = "Nonce.", in = ParameterIn.QUERY, required = false),
                    @Parameter(name = "audience", description = "Audience.", in = ParameterIn.QUERY, required = false),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "MultiValueMap",
                    content = { @Content( mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE, schema = @Schema(implementation = MultiValueMap.class) ) },
                    required = false),
            responses = {
                    @ApiResponse(description = "Access Tokens", responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenResponse.class))
                    )
            })
    @PostMapping(path = "/{project-id}/token",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
    public ResponseEntity<TokenResponse> postGetTokens(@PathVariable("project-id") String projectId,
                                                       @RequestParam(name = "grant_type", required = true) String grantType,
                                                       @RequestParam(name = "username", required = false) String username,
                                                       @RequestParam(name = "password", required = false) String password,
                                                       @RequestParam(name = "scope", required = false) String scope,
                                                       @RequestParam(name = "client_id", required = false) String clientId,
                                                       @RequestParam(name = "client_secret",  required = false) String clientSecret,
                                                       @RequestParam(name = "refresh_token", required = false) String refreshToken,
                                                       @RequestParam(name = "code", required = false) String code,
                                                       @RequestParam(name = "nonce", required = false) String nonce,
                                                       @RequestParam(name = "audience", required = false) String audience,
                                                       @RequestBody MultiValueMap bodyValueMap,
                                                       HttpServletRequest request) throws MalformedURLException, URISyntaxException {
        String servletContextPath = servletContext.getContextPath();
        URL requestUrl = new URL(request.getRequestURL().toString());
        URI issuerUri = getIssuerUri(servletContextPath, requestUrl, projectId, urlMapper);
        GrantType grantTypeEnum = GrantType.getGrantType(grantType);
        Set<String> scopes = getScopes(scope);
        switch (grantTypeEnum) {
            case PASSWORD -> {
                Optional<ClientCredentials> ccOptional = getClientCredentials(request, clientId, clientSecret);
                if (ccOptional.isPresent()) {
                    UserCredentials userCredentials = new UserCredentials(username, password);
                    TokenResponse tokenResponse = oAuth2Service.getTokenForPassword(issuerUri, projectId, ccOptional.get(), audience, scopes, userCredentials);
                    return ResponseEntity.ok(tokenResponse);
                } else {
                    throw new OAuth2TokenException("Missing or invalid client credentials.");
                }
            }
            case CLIENT_CREDENTIALS -> {
                Optional<ClientCredentials> ccOptional = getClientCredentials(request, clientId, clientSecret);
                if (ccOptional.isPresent()) {
                    TokenResponse tokenResponse = oAuth2Service.getTokenForClientCredentials(issuerUri, projectId, ccOptional.get(), audience, scopes);
                    return ResponseEntity.ok(tokenResponse);
                } else {
                    throw new OAuth2TokenException("Missing or invalid client credentials.");
                }
            }
            case REFRESH_TOKEN -> {
                Optional<ClientCredentials> ccOptional = getClientCredentials(request, clientId, clientSecret);
                if (ccOptional.isPresent()) {
                    TokenResponse tokenResponse = oAuth2Service.getTokenForRefreshToken(projectId, refreshToken, ccOptional.get());
                    return ResponseEntity.ok(tokenResponse);
                } else {
                    throw new OAuth2TokenException("Missing or invalid client credentials.");
                }
            }
            default -> {
                throw new OAuth2TokenException("Unsupported grant type: " + grantType);
            }
        }
    }


    @Operation(description = "__OpenID Connect Discovery__ \n" +
            "Get information about this OAuth2 server configuration. \n" +
            "- [OpenID Connect Discovery](https://openid.net/specs/openid-connect-discovery-1_0.html)")
    @GetMapping(path = "/{project-id}/.well-known/openid-configuration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProviderConfigurationResponse> getConfiguration(@PathVariable("project-id") String projectId,
                                                                          HttpServletRequest request) throws MalformedURLException, URISyntaxException {
        LOGGER.info("getConfiguration: {}", request.getRequestURL());
        String servletContextPath = servletContext.getContextPath();
        URL requestUrl = new URL(request.getRequestURL().toString());
        URI issuerUri = getIssuerUri(servletContextPath, requestUrl, projectId, urlMapper);
        ProviderConfigurationResponse configuration = oAuth2Service.getProviderConfiguration(issuerUri, projectId);
        return ResponseEntity.ok(configuration);
    }

    @Operation(description = "__Get JSON Web Keys (JWK)__ \n" +
            "Get all available public keys on the project. A JSON Web Key (JWK) is a JavaScript Object Notation (JSON) data\n" +
            "structure that represents a cryptographic key. \n" +
            "- [RFC7517](https://tools.ietf.org/html/rfc7517)")
    @GetMapping(path = "/{project-id}/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWKResponse> getCerts(@PathVariable("project-id") String projectId) {
        LOGGER.info("getCerts: projectId={}", projectId);
        JWKResponse jwkData = oAuth2Service.getJWKResponse(projectId);
        return ResponseEntity.ok(jwkData);
    }

    @Operation(description = "__OAuth 2.0 Token Introspection__ \n" +
            "Get active state about provided token." +
            "- [RFC7662](https://tools.ietf.org/html/rfc7662)")
    @PostMapping(path = "/{project-id}/introspect", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IntrospectResponse> introspectToken(@PathVariable("project-id") String projectId,
                                                              @RequestParam("token") String token,
                                                              @RequestParam(name = "token_type_hint", required = false) String tokenTypeHint) {
        LOGGER.info("introspectToken: token={} token_type_hint={}", token, tokenTypeHint);
        IntrospectResponse response = oAuth2Service.getIntrospectResponse(projectId, token, tokenTypeHint);
        return ResponseEntity.ok(response);
    }

    @Operation(description = "__OAuth 2.0 Token Revocation__ \n" +
            "Revoke issued and valid token which is no longer needed." +
            "- [RFC7009](https://tools.ietf.org/html/rfc7009)")
    @PostMapping(path = "/{project-id}/revoke", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Void> revoke(@PathVariable("project-id") String projectId,
                                       @RequestParam("token") String token,
                                       @RequestParam(name = "token_type_hint", required = false) String tokenTypeHint) {
        oAuth2Service.revoke(projectId, token, tokenTypeHint);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "__UserInfo Request__ \n" +
            "Get info about User\n" +
            "- [OICD/UserInfoRequest](https://openid.net/specs/openid-connect-core-1_0.html#UserInfoRequest)")
    @GetMapping(path = "/{project-id}/userinfo", produces = MediaType.APPLICATION_JSON_VALUE )
    @PostMapping(path = "/{project-id}/userinfo", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable("project-id") String projectId,
                                                        HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            String token = authorization.substring(BEARER_PREFIX.length());
            Optional<UserInfoResponse> response = oAuth2Service.getUserInfo(projectId, token);
            return ResponseEntity.of(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
