package one.microproject.authx.jclient.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.microproject.authx.common.dto.AuthxInfo;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.jclient.AuthXOAuth2Client;

import java.io.IOException;

import static one.microproject.authx.jclient.impl.Constants.DELIMITER;
import static one.microproject.authx.jclient.impl.Constants.SERVICES_SYSTEM;

public class AuthXClientImpl implements AuthXClient {

    private final String baseUrl;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public AuthXClientImpl(String baseUrl, OkHttpClient client, ObjectMapper mapper) {
        this.baseUrl = baseUrl;
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public AuthxInfo getAuthxInfo() {
        try {
            Request request = new Request.Builder()
                    .url(baseUrl + SERVICES_SYSTEM + DELIMITER + "info")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                return mapper.readValue(response.body().string(), AuthxInfo.class);
            } else {
                throw new AuthXClientException("GET AuthxInfo Error: " + response.code());
            }
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    @Override
    public AuthXOAuth2Client getAuthXOAuth2Client(String projectId) {
        return new AuthXOAuth2ClientImpl(baseUrl, projectId, client, mapper);
    }

}
