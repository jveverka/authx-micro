package one.microproject.authx.service.tests.service;

import one.microproject.authx.service.service.impl.UrlMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlMapperTest {

    private static final UrlMapper urlMapper = new UrlMapper(
            "http://localhost:8080/authx",
            "https://myserver.domain.com/services/authx"
    );

    public static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of("http://authx:8080/authx", "http://authx:8080/authx"),
                Arguments.of("http://localhost:8080/authx", "https://myserver.domain.com/services/authx"),
                Arguments.of("http://localhost:8080/authx/token", "https://myserver.domain.com/services/authx/token")
        );
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testUrlMapper(String requestUrl, String expectedValue) {
        String mapperUrl = urlMapper.map(requestUrl);
        assertEquals(expectedValue, mapperUrl);
    }

}
