package one.microproject.authx.jclient.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import one.microproject.authx.common.dto.AuthXResponse;
import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.Empty;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.*;
import one.microproject.authx.jclient.AuthXOAuth2Client;

import java.io.IOException;
import java.util.Set;

import static one.microproject.authx.common.Constants.BEARER_PREFIX;
import static one.microproject.authx.common.Urls.DELIMITER;
import static one.microproject.authx.common.Urls.INTROSPECT;
import static one.microproject.authx.common.Urls.JWKS;
import static one.microproject.authx.common.Urls.OPENID_CONFIG;
import static one.microproject.authx.common.Urls.REVOKE;
import static one.microproject.authx.common.Urls.SERVICES_OAUTH2;
import static one.microproject.authx.common.Urls.TOKEN;
import static one.microproject.authx.common.Urls.USERINFO;
import static one.microproject.authx.common.utils.TokenUtils.mapScopes;

public class AuthXOAuth2ClientImpl implements AuthXOAuth2Client {

    public static final String AUTHORIZATION = "Authorization";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";


    private final String baseUrl;
    private final String projectId;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public AuthXOAuth2ClientImpl(String baseUrl, String projectId, OkHttpClient client, ObjectMapper mapper) {
        this.baseUrl = baseUrl;
        this.projectId = projectId;
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public AuthXResponse<TokenResponse, Void> getTokenForClient(ClientCredentials clientCredentials, String audience, Set<String> scopes) {
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + SERVICES_OAUTH2 + DELIMITER + projectId + TOKEN)
                    .newBuilder();
            httpBuilder.addQueryParameter("grant_type", "client_credentials");
            httpBuilder.addQueryParameter("scope", mapScopes(scopes));
            httpBuilder.addQueryParameter("audience", audience);
            httpBuilder.addQueryParameter("client_id", clientCredentials.id());
            httpBuilder.addQueryParameter("client_secret", clientCredentials.secret());
            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .post(RequestBody.create("{}", MediaType.parse(APPLICATION_FORM_URLENCODED)))
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                TokenResponse tokenResponse = mapper.readValue(response.body().string(), TokenResponse.class);
                return AuthXResponse.ok(tokenResponse, response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<TokenResponse, Void> getTokenForPassword(ClientCredentials clientCredentials, String audience, Set<String> scopes, UserCredentials userCredentials) {
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + SERVICES_OAUTH2 + DELIMITER + projectId + TOKEN)
                    .newBuilder();
            httpBuilder.addQueryParameter("grant_type", "password");
            httpBuilder.addQueryParameter("username", userCredentials.username());
            httpBuilder.addQueryParameter("scope", mapScopes(scopes));
            httpBuilder.addQueryParameter("audience", audience);
            httpBuilder.addQueryParameter("password", userCredentials.password());
            httpBuilder.addQueryParameter("client_id", clientCredentials.id());
            httpBuilder.addQueryParameter("client_secret", clientCredentials.secret());
            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .post(RequestBody.create("{}", MediaType.parse(APPLICATION_FORM_URLENCODED)))
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                TokenResponse tokenResponse = mapper.readValue(response.body().string(), TokenResponse.class);
                return AuthXResponse.ok(tokenResponse, response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<IntrospectResponse, Void> introspect(String token, String typeHint) {
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + SERVICES_OAUTH2 + DELIMITER + projectId + INTROSPECT)
                    .newBuilder();
            httpBuilder.addQueryParameter("token", token);
            if (typeHint != null) {
                httpBuilder.addQueryParameter("token_type_hint", typeHint);
            }
            Request request = new Request.Builder()
                .url(httpBuilder.build())
                .post(RequestBody.create("{}", MediaType.parse(APPLICATION_JSON)))
                .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                IntrospectResponse introspectResponse = mapper.readValue(response.body().string(), IntrospectResponse.class);
                return AuthXResponse.ok(introspectResponse, response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<Empty, Void> revoke(String token, String typeHint) {
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + SERVICES_OAUTH2 + DELIMITER + projectId + REVOKE)
                    .newBuilder();
            httpBuilder.addQueryParameter("token", token);
            if (typeHint != null) {
                httpBuilder.addQueryParameter("token_type_hint", typeHint);
            }
            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .post(RequestBody.create("{}", MediaType.parse(APPLICATION_JSON)))
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return AuthXResponse.ok(response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<TokenResponse, Void> refreshToken(ClientCredentials clientCredentials, String refreshToken) {
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + SERVICES_OAUTH2 + DELIMITER + projectId + TOKEN)
                    .newBuilder();
            httpBuilder.addQueryParameter("grant_type", "refresh_token");
            httpBuilder.addQueryParameter("refresh_token", refreshToken);
            httpBuilder.addQueryParameter("client_id", clientCredentials.id());
            httpBuilder.addQueryParameter("client_secret", clientCredentials.secret());
            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .post(RequestBody.create("{}", MediaType.parse(APPLICATION_FORM_URLENCODED)))
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                TokenResponse tokenResponse = mapper.readValue(response.body().string(), TokenResponse.class);
                return AuthXResponse.ok(tokenResponse, response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<ProviderConfigurationResponse, Void> getConfiguration() {
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + SERVICES_OAUTH2 + DELIMITER + projectId + OPENID_CONFIG)
                    .newBuilder();
            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                ProviderConfigurationResponse providerConfigurationResponse = mapper.readValue(response.body().string(), ProviderConfigurationResponse.class);
                return AuthXResponse.ok(providerConfigurationResponse, response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<JWKResponse, Void> getCerts() {
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + SERVICES_OAUTH2 + DELIMITER + projectId + JWKS)
                    .newBuilder();
            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                JWKResponse jwkResponse = mapper.readValue(response.body().string(), JWKResponse.class);
                return AuthXResponse.ok(jwkResponse, response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXResponse<UserInfoResponse, Void> getUserInfo(String token) {
        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + SERVICES_OAUTH2 + DELIMITER + projectId + USERINFO)
                    .newBuilder();
            Request request = new Request.Builder()
                    .url(httpBuilder.build())
                    .addHeader(AUTHORIZATION, BEARER_PREFIX + token)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                UserInfoResponse userInfoResponse = mapper.readValue(response.body().string(), UserInfoResponse.class);
                return AuthXResponse.ok(userInfoResponse, response.code());
            } else {
                return AuthXResponse.error(response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

}
