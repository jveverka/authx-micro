package one.microproject.authx.jclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import one.microproject.authx.jclient.impl.AuthXClientImpl;

public class AuthXClientBuilder {

    private String baseUrl = "http://localhost:8080/authx";
    private OkHttpClient client = new OkHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    public AuthXClientBuilder withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public AuthXClientBuilder withClient(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public AuthXClientBuilder withMapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public AuthXClient build() {
        return new AuthXClientImpl(baseUrl, client, mapper);
    }
}
