package one.microproject.authx.service.tests.controller;

import one.microproject.authx.common.dto.*;
import one.microproject.authx.common.dto.oauth2.IntrospectResponse;
import one.microproject.authx.common.dto.oauth2.JWKResponse;
import one.microproject.authx.common.dto.oauth2.ProviderConfigurationResponse;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.common.dto.oauth2.UserInfoResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.jclient.AuthXOAuth2Client;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        AuthXResponse<TokenResponse, Void> response = authXOAuth2Client.getTokenForPassword(clientCredentials, "", scopes, userCredentials);

        assertTrue(response.isSuccess());
        TokenResponse tokensForPassword = response.response();
        assertNotNull(tokensForPassword);
        assertNotNull(tokensForPassword.getAccessToken());
        assertNotNull(tokensForPassword.getRefreshToken());

        AuthXResponse<IntrospectResponse,Void> introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());

        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());

        authXOAuth2Client.revoke(tokensForPassword.getAccessToken(), "");
        authXOAuth2Client.revoke(tokensForPassword.getRefreshToken(), "");

        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getAccessToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.isSuccess());
        assertFalse(introspectResponse.response().getActive());

        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getRefreshToken(), "");
        assertNotNull(introspectResponse);
        assertTrue(introspectResponse.isSuccess());
        assertFalse(introspectResponse.response().getActive());
    }

    @Test
    void testRefreshTokenForUsernameAndPassword() {
        AuthXOAuth2Client authXOAuth2Client = getGlobalAdminOAuth2Client();
        ClientCredentials clientCredentials = new ClientCredentials("admin-client", "secret");
        Set<String> scopes = Set.of();
        UserCredentials userCredentials = new UserCredentials("admin-user", "s3cr3t");

        AuthXResponse<TokenResponse, Void> response = authXOAuth2Client.getTokenForPassword(clientCredentials, "", scopes, userCredentials);
        assertTrue(response.isSuccess());
        TokenResponse tokensForPassword = response.response();

        AuthXResponse<IntrospectResponse, Void> introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getAccessToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getRefreshToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());

        response = authXOAuth2Client.refreshToken(clientCredentials, tokensForPassword.getRefreshToken());
        assertTrue(response.isSuccess());
        TokenResponse tokensForRefresh = response.response();

        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getAccessToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertFalse(introspectResponse.response().getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForPassword.getRefreshToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());

        introspectResponse = authXOAuth2Client.introspect(tokensForRefresh.getAccessToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForRefresh.getRefreshToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());
    }

    @Test
    void testGetTokenForClient() {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
        BuildProjectRequest buildProjectRequest = createBuildProjectRequest("pid-002");
        AuthXResponse<String, ErrorMessage> responseMessage = authXClient.buildProject(globalAdminTokens.getAccessToken(), buildProjectRequest);
        assertTrue(responseMessage.isSuccess());

        AuthXOAuth2Client authXOAuth2Client = authXClient.getAuthXOAuth2Client("pid-002");

        ClientCredentials clientCredentials = new ClientCredentials("cl-001", "secret");
        Set<String> scopes = Set.of();
        AuthXResponse<TokenResponse, Void> response = authXOAuth2Client.getTokenForClient(clientCredentials, "", scopes);
        assertTrue(response.isSuccess());
        TokenResponse tokensForClient = response.response();
        assertNotNull(tokensForClient);
        assertNotNull(tokensForClient.getAccessToken());
        assertNotNull(tokensForClient.getRefreshToken());

        AuthXResponse<IntrospectResponse, Void> introspectResponse = authXOAuth2Client.introspect(tokensForClient.getAccessToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());

        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getRefreshToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());

        authXOAuth2Client.revoke(tokensForClient.getAccessToken(), "");
        authXOAuth2Client.revoke(tokensForClient.getRefreshToken(), "");

        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getAccessToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertFalse(introspectResponse.response().getActive());

        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getRefreshToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertFalse(introspectResponse.response().getActive());

        responseMessage = authXClient.deleteProject(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertTrue(responseMessage.isSuccess());
    }

    @Test
    void testRefreshTokenForClient() {
        TokenResponse globalAdminTokens = getGlobalAdminTokens();
        AuthXClient authXClient = getAuthXClient();
        BuildProjectRequest buildProjectRequest = createBuildProjectRequest("pid-003");
        AuthXResponse<String, ErrorMessage> responseMessage = authXClient.buildProject(globalAdminTokens.getAccessToken(), buildProjectRequest);
        assertTrue(responseMessage.isSuccess());

        AuthXOAuth2Client authXOAuth2Client = authXClient.getAuthXOAuth2Client("pid-003");
        ClientCredentials clientCredentials = new ClientCredentials("cl-001", "secret");
        Set<String> scopes = Set.of();

        AuthXResponse<TokenResponse, Void> response = authXOAuth2Client.getTokenForClient(clientCredentials, "", scopes);
        assertTrue(response.isSuccess());
        TokenResponse tokensForClient = response.response();

        AuthXResponse<IntrospectResponse, Void> introspectResponse = authXOAuth2Client.introspect(tokensForClient.getAccessToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getRefreshToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());

        response = authXOAuth2Client.refreshToken(clientCredentials, tokensForClient.getRefreshToken());
        assertTrue(response.isSuccess());
        TokenResponse tokensForRefresh = response.response();

        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getAccessToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertFalse(introspectResponse.response().getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForClient.getRefreshToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForRefresh.getAccessToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());
        introspectResponse = authXOAuth2Client.introspect(tokensForRefresh.getRefreshToken(), "");
        assertTrue(introspectResponse.isSuccess());
        assertTrue(introspectResponse.response().getActive());

        responseMessage = authXClient.deleteProject(globalAdminTokens.getAccessToken(), buildProjectRequest.createProjectRequest().id());
        assertTrue(responseMessage.isSuccess());
    }

    @Test
    void testProviderConfiguration() {
        AuthXOAuth2Client authXOAuth2Client = getGlobalAdminOAuth2Client();
        AuthXResponse<ProviderConfigurationResponse, Void> response = authXOAuth2Client.getConfiguration();
        assertTrue(response.isSuccess());
        ProviderConfigurationResponse providerConfigurationResponse = response.response();
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

    @Test
    void testGetCerts() {
        AuthXOAuth2Client authXOAuth2Client = getGlobalAdminOAuth2Client();
        AuthXResponse<JWKResponse, Void> certs = authXOAuth2Client.getCerts();
        assertTrue(certs.isSuccess());
        assertNotNull(certs.response().getKeys());
        assertEquals(1, certs.response().getKeys().size());
    }

    @Test
    void testUserInfo() {
        AuthXOAuth2Client authXOAuth2Client = getGlobalAdminOAuth2Client();
        TokenResponse globalAdminTokens = getGlobalAdminTokens();

        AuthXResponse<UserInfoResponse, Void> userInfo = authXOAuth2Client.getUserInfo(globalAdminTokens.getAccessToken());
        assertTrue(userInfo.isSuccess());
        assertNotNull(userInfo.response().getSub());
        assertEquals("admin-user", userInfo.response().getSub());
    }

}
