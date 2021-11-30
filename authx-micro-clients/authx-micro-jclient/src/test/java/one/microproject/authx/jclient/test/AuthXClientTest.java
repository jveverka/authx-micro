package one.microproject.authx.jclient.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.jclient.AuthXClientBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static one.microproject.authx.jclient.impl.Constants.DELIMITER;
import static one.microproject.authx.jclient.impl.Constants.SERVICES_OAUTH2;
import static one.microproject.authx.jclient.impl.Constants.TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthXClientTest {

    private static ObjectMapper mapper;

    private MockWebServer server;

    @BeforeAll
    public static void init() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void beforeEach() {
        server = new MockWebServer();
    }

    @Test
    void testGetTokenForPassword() throws JsonProcessingException {
        String projectId = "project-001";
        String url = server.url(SERVICES_OAUTH2 + DELIMITER + projectId).toString();
        TokenResponse tokenResponse = new TokenResponse("access", 1L, 2L, "refresh", "type", "id");
        ClientCredentials clientCredentials = new ClientCredentials("client-id", "secret");
        Set<String> scopes = Set.of();
        UserCredentials userCredentials = new UserCredentials("user", "password");
        
        AuthXClient authXClient = new AuthXClientBuilder()
                .withBaseUrl(url)
                .withProjectId(projectId)
                .build();
        server.enqueue(new MockResponse().setBody(mapper.writeValueAsString(tokenResponse)));

        TokenResponse response = authXClient.getTokenForPassword(clientCredentials, scopes, userCredentials);
        assertEquals(tokenResponse.getAccessToken(), response.getAccessToken());
        assertEquals(tokenResponse.getExpiresIn(), response.getExpiresIn());
        assertEquals(tokenResponse.getRefreshExpiresIn(), response.getRefreshExpiresIn());
        assertEquals(tokenResponse.getRefreshToken(), response.getRefreshToken());
        assertEquals(tokenResponse.getIdToken(), response.getIdToken());
    }

    @AfterEach
    private void afterEach() throws IOException {
        server.shutdown();
    }

    @AfterAll
    public static void shutdown() {
    }

}
