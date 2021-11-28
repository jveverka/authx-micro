package one.microproject.authx.jclient;

import one.microproject.authx.jclient.impl.AuthXClientImpl;

public class AuthXClientBuilder {

    private String baseUrl;
    private String projectId;

    public AuthXClientBuilder withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public AuthXClientBuilder withProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public AuthXClient build() {
        return new AuthXClientImpl(baseUrl, projectId);
    }

}
