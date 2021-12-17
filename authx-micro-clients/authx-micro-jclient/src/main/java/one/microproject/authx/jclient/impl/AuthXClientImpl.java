package one.microproject.authx.jclient.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;

import java.io.IOException;
import java.util.Set;

import static one.microproject.authx.common.utils.TokenUtils.mapScopes;
import static one.microproject.authx.jclient.impl.Constants.CLIENT_ID;
import static one.microproject.authx.jclient.impl.Constants.CLIENT_SECRET;
import static one.microproject.authx.jclient.impl.Constants.DELIMITER;
import static one.microproject.authx.jclient.impl.Constants.INTROSPECT;
import static one.microproject.authx.jclient.impl.Constants.REVOKE;
import static one.microproject.authx.jclient.impl.Constants.SERVICES_OAUTH2;
import static one.microproject.authx.jclient.impl.Constants.TOKEN;
import static one.microproject.authx.jclient.impl.Constants.SCOPE;

public class AuthXClientImpl implements AuthXClient {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";


    private final String baseUrl;
    private final String projectId;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public AuthXClientImpl(String baseUrl, String projectId, OkHttpClient client, ObjectMapper mapper) {
        this.baseUrl = baseUrl;
        this.projectId = projectId;
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public TokenResponse getTokenForPassword(ClientCredentials clientCredentials, String audience, Set<String> scopes, UserCredentials userCredentials) {
        try {
            Request request = new Request.Builder()
                    .url(baseUrl + SERVICES_OAUTH2 + DELIMITER + projectId + TOKEN +
                            "?grant_type=password" +
                            "&username=" + userCredentials.username() +
                            SCOPE + mapScopes(scopes) +
                            "&audience=" + audience +
                            "&password=" + userCredentials.password() +
                            CLIENT_ID + clientCredentials.id() +
                            CLIENT_SECRET + clientCredentials.secret())
                    .post(RequestBody.create("{}", MediaType.parse(APPLICATION_FORM_URLENCODED)))
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return mapper.readValue(response.body().string(), TokenResponse.class);
            } else {
                throw new AuthXClientException("Username/Password Auth Error: " + response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public IntrospectResponse introspect(String token, String typeHint) {
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
                return mapper.readValue(response.body().string(), IntrospectResponse.class);
            } else {
                throw new AuthXClientException("Token Introspect Error: " + response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public void revoke(String token, String typeHint) {
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
            if (response.code() != 200) {
                throw new AuthXClientException("Token Revoke Error: " + response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }


}
