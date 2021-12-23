package one.microproject.authx.service.tests.controller;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.dto.TokenClaims;
import one.microproject.authx.common.dto.TokenType;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.common.utils.TokenUtils;
import one.microproject.authx.jclient.impl.AuthXClientException;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static one.microproject.authx.jclient.impl.AuthXOAuth2ClientImpl.APPLICATION_JSON;
import static one.microproject.authx.jclient.impl.AuthXOAuth2ClientImpl.AUTHORIZATION;
import static one.microproject.authx.jclient.impl.Constants.DELIMITER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityTests extends AppBaseTest {

    private OkHttpClient client = new OkHttpClient();

    private static Stream<Arguments> provideForTestPutAccessNoToken() {
        return Stream.of(
                Arguments.of("/api/v1/admin/authx/build")
        );
    }

    private static Stream<Arguments> provideForTestDeleteAccessNoToken() {
        return Stream.of(
                Arguments.of("/api/v1/admin/authx/project/p-001")
        );
    }

    private static Stream<Arguments> provideForTestGetAccessNoToken() {
        return Stream.of(
                Arguments.of("/api/v1/admin/project/projects/p-001")
        );
    }

    private static Stream<Arguments> provideForTestPostAccessNoToken() {
        return Stream.of(
                Arguments.of("/api/v1/admin/project/projects/p-001")
        );
    }

    @ParameterizedTest()
    @MethodSource("provideForTestPutAccessNoToken")
    void testPutAccessNoToken(String url) {
        assertEquals(401, put(null, url));
        assertEquals(401, put("", url));
        assertEquals(401, put("Bearer", url));
        assertEquals(401, put("Bearer ", url));
        assertEquals(401, put("Bearer xxxxx", url));
        assertEquals(401, put("Bearer xxxxx.xxxxxx.xxxxx", url));
        assertEquals(401, put("Bearer " + generateValidButUnregisteredToken(), url));
    }

    @ParameterizedTest()
    @MethodSource("provideForTestDeleteAccessNoToken")
    void testDeleteAccessNoToken(String url) {
        assertEquals(401, delete(null, url));
        assertEquals(401, delete("", url));
        assertEquals(401, delete("Bearer", url));
        assertEquals(401, delete("Bearer ", url));
        assertEquals(401, delete("Bearer xxxxx", url));
        assertEquals(401, delete("Bearer xxxxx.xxxxxx.xxxxx", url));
        assertEquals(401, delete("Bearer " + generateValidButUnregisteredToken(), url));
    }

    @ParameterizedTest()
    @MethodSource("provideForTestGetAccessNoToken")
    void testDeleteGetNoToken(String url) {
        assertEquals(401, get(null, url));
        assertEquals(401, get("", url));
        assertEquals(401, get("Bearer", url));
        assertEquals(401, get("Bearer ", url));
        assertEquals(401, get("Bearer xxxxx", url));
        assertEquals(401, get("Bearer xxxxx.xxxxxx.xxxxx", url));
        assertEquals(401, get("Bearer " + generateValidButUnregisteredToken(), url));
    }

    @ParameterizedTest()
    @MethodSource("provideForTestPostAccessNoToken")
    void testDeletePostNoToken(String url) {
        assertEquals(401, post(null, url));
        assertEquals(401, post("", url));
        assertEquals(401, post("Bearer", url));
        assertEquals(401, post("Bearer ", url));
        assertEquals(401, post("Bearer xxxxx", url));
        assertEquals(401, post("Bearer xxxxx.xxxxxx.xxxxx", url));
        assertEquals(401, post("Bearer " + generateValidButUnregisteredToken(), url));
    }

    private int put(String token, String url) {
        try {
            Request.Builder builder = new Request.Builder()
                    .url(getBaseUrl() + DELIMITER + url)
                    .put(RequestBody.create("{}", MediaType.parse(APPLICATION_JSON)));
            if (token != null) {
                builder.header(AUTHORIZATION, token);
            }
            Response response = client.newCall(builder.build()).execute();
            return response.code();
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    private int post(String token, String url) {
        try {
            Request.Builder builder = new Request.Builder()
                    .url(getBaseUrl() + DELIMITER + url)
                    .post(RequestBody.create("{}", MediaType.parse(APPLICATION_JSON)));
            if (token != null) {
                builder.header(AUTHORIZATION, token);
            }
            Response response = client.newCall(builder.build()).execute();
            return response.code();
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    private int delete(String token, String url) {
        try {
            Request.Builder builder = new Request.Builder()
                    .url(getBaseUrl() + DELIMITER + url)
                    .delete();
            if (token != null) {
                builder.header(AUTHORIZATION, token);
            }
            Response response = client.newCall(builder.build()).execute();
            return response.code();
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    private int get(String token, String url) {
        try {
            Request.Builder builder = new Request.Builder()
                    .url(getBaseUrl() + DELIMITER + url)
                    .get();
            if (token != null) {
                builder.header(AUTHORIZATION, token);
            }
            Response response = client.newCall(builder.build()).execute();
            return response.code();
        } catch (IOException e) {
            throw new AuthXClientException(e);
        }
    }

    private static String generateValidButUnregisteredToken() {
        String jti = UUID.randomUUID().toString();
        Long epochMilli = Instant.now().getEpochSecond() * 1000L;
        Date issuedAt = new Date(epochMilli);
        Date accessExpiration = new Date(epochMilli + 20*1000L);
        KeyPairData keyPairData = CryptoUtils.generateKeyPair("kid-001", "iss", "iss", Instant.now(), TimeUnit.HOURS, 1L);
        TokenClaims claims = new TokenClaims("iss", "sub", "aud", Set.of(), issuedAt, accessExpiration, TokenType.BEARER, jti, "p-001");
        return TokenUtils.issueToken(claims, keyPairData.id(), keyPairData.privateKey());
    }

}
