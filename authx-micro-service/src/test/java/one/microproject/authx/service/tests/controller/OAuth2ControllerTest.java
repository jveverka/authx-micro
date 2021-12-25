package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ClientCredentials;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.common.dto.UserCredentials;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.ProviderConfigurationResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.jclient.AuthXOAuth2Client;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OAuth2ControllerTest extends AppBaseTest  {

    @Test
    void testGetTokenForUsernameAndPassword() {
        AuthXOAuth2Client authXOAuth2Client = getGlobalAdminOAuth2Client();
        ClientCredentials clientCredentials = new ClientCredentials("admin-client", "secret");
        Set<String> scopes = Set.of();
        UserCredentials userCredentials = new UserCredentials("admin-user", "s3cr3t");
        TokenResponse tokensForPassword = authXOAuth2Client.getTokenForPassword(clientCredentials, "", scopes, userCredentials);
        assertNotNull(tokensForPassword);
        assertNotNull(tokensForPassword.getAccessToken());
        assertNotNull(tokensForPassword.getRefreshToken());

        IntrospectResponse introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.getActive());

        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.getActive());

        authXOAuth2Client.revoke(tokensForPassword.getAccessToken(), "");
        authXOAuth2Client.revoke(tokensForPassword.getRefreshToken(), "");

        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertFalse(introspectResponse.getActive());

        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertFalse(introspectResponse.getActive());
    }

    @Test
    void testRefreshTokenForUsernameAndPassword() {
        AuthXOAuth2Client authXOAuth2Client = getGlobalAdminOAuth2Client();
        ClientCredentials clientCredentials = new ClientCredentials("admin-client", "secret");
        Set<String> scopes = Set.of();
        UserCredentials userCredentials = new UserCredentials("admin-user", "s3cr3t");
        TokenResponse tokensForPassword = authXOAuth2Client.getTokenForPassword(clientCredentials, "", scopes, userCredentials);

        IntrospectResponse introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getAccessToken(), "");
        assertTrue(introspectResponse.getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getRefreshToken(), "");
        assertTrue(introspectResponse.getActive());

        TokenResponse tokensForRefresh = authXOAuth2Client.refreshToken(clientCredentials, tokensForPassword.getRefreshToken());

        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getAccessToken(), "");
        assertFalse(introspectResponse.getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getRefreshToken(), "");
        assertTrue(introspectResponse.getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForRefresh.getAccessToken(), "");
        assertTrue(introspectResponse.getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForRefresh.getRefreshToken(), "");
        assertTrue(introspectResponse.getActive());
    }

    @Test
    void testGetTokenForClient() {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
        BuildProjectRequest buildProjectRequest = createBuildProjectRequest("pid-002");
        ResponseMessage responseMessage = authXClient.buildProject(globalAdminTokens.getAccessToken(), buildProjectRequest);
        assertTrue(responseMessage.success());

        AuthXOAuth2Client authXOAuth2Client = authXClient.getAuthXOAuth2Client("pid-002");

        ClientCredentials clientCredentials = new ClientCredentials("cl-001", "secret");
        Set<String> scopes = Set.of();
        TokenResponse tokensForClient = authXOAuth2Client.getTokenForClient(clientCredentials, "", scopes);
        assertNotNull(tokensForClient);
        assertNotNull(tokensForClient.getAccessToken());
        assertNotNull(tokensForClient.getRefreshToken());

        IntrospectResponse introspectResponse = authXOAuth2Client.introspect(tokensForClient.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.getActive());

        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.getActive());

        authXOAuth2Client.revoke(tokensForClient.getAccessToken(), "");
        authXOAuth2Client.revoke(tokensForClient.getRefreshToken(), "");

        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertFalse(introspectResponse.getActive());

        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertFalse(introspectResponse.getActive());

        responseMessage = authXClient.deleteProject(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertTrue(responseMessage.success());
    }

    @Test
    void testRefreshTokenForClient() {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
        BuildProjectRequest buildProjectRequest = createBuildProjectRequest("pid-003");
        ResponseMessage responseMessage = authXClient.buildProject(globalAdminTokens.getAccessToken(), buildProjectRequest);
        assertTrue(responseMessage.success());

        AuthXOAuth2Client authXOAuth2Client = authXClient.getAuthXOAuth2Client("pid-003");
        ClientCredentials clientCredentials = new ClientCredentials("cl-001", "secret");
        Set<String> scopes = Set.of();

        TokenResponse tokensForClient = authXOAuth2Client.getTokenForClient(clientCredentials, "", scopes);

        IntrospectResponse introspectResponse = authXOAuth2Client.introspect(tokensForClient.getAccessToken(), "");
        assertTrue(introspectResponse.getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getRefreshToken(), "");
        assertTrue(introspectResponse.getActive());

        TokenResponse tokensForRefresh = authXOAuth2Client.refreshToken(clientCredentials, tokensForClient.getRefreshToken());

        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getAccessToken(), "");
        assertFalse(introspectResponse.getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getRefreshToken(), "");
        assertTrue(introspectResponse.getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForRefresh.getAccessToken(), "");
        assertTrue(introspectResponse.getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForRefresh.getRefreshToken(), "");
        assertTrue(introspectResponse.getActive());

        responseMessage = authXClient.deleteProject(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertTrue(responseMessage.success());
    }

    @Test
    void testProviderConfigurationTest() {
        AuthXOAuth2Client authXOAuth2Client = getGlobalAdminOAuth2Client();
        ProviderConfigurationResponse providerConfigurationResponse = authXOAuth2Client.getConfiguration();
        assertNotNull(providerConfigurationResponse);
        assertNotNull(providerConfigurationResponse.getAuthorizationEndpoint());
        assertNotNull(providerConfigurationResponse.getGrantTypesSupported());
        assertNotNull(providerConfigurationResponse.getIdTokenEncryptionAlgValuesSupported());
        assertNotNull(providerConfigurationResponse.getIssuer());
        assertNotNull(providerConfigurationResponse.getIdTokenSigningAlgValuesSupported());
        assertNotNull(providerConfigurationResponse.getIntrospectionEndpoint());
        assertNotNull(providerConfigurationResponse.getJwksUri());
        assertNotNull(providerConfigurationResponse.getResponseTypesSupported());
        assertNotNull(providerConfigurationResponse.getRevocationEndpoint());
        assertNotNull(providerConfigurationResponse.getScopesSupported());
        assertNotNull(providerConfigurationResponse.getSubjectTypesSupported());
    }

}
