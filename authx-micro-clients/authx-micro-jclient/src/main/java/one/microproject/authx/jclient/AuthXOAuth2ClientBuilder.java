package one.microproject.authx.jclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import one.microproject.authx.jclient.impl.AuthXOAuth2ClientImpl;

public class AuthXOAuth2ClientBuilder {

    private String baseUrl = "http://localhost:8080/authx";
    private String projectId = "global-admins";
    private OkHttpClient client = new OkHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    public AuthXOAuth2ClientBuilder withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public AuthXOAuth2ClientBuilder withProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public AuthXOAuth2ClientBuilder withClient(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public AuthXOAuth2ClientBuilder withMapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public AuthXOAuth2Client build() {
        return new AuthXOAuth2ClientImpl(baseUrl, projectId, client, mapper);
    }

}
