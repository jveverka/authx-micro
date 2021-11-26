package one.microproject.authx.common.tests;

import one.microproject.authx.common.exceptions.DataProcessingException;
import one.microproject.authx.common.utils.LabelUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static one.microproject.authx.common.utils.LabelUtils.AUTHX_CERTIFICATE_DURATION;
import static one.microproject.authx.common.utils.LabelUtils.AUTHX_LOGIN_ENABLED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LabelUtilsTest {

    public static Stream<Arguments> provideParametersForMerge() {
        return Stream.of(
                Arguments.of(Map.of(), LabelUtils.defaults)
        );
    }

    public static Stream<Arguments> provideParametersForTest() {
        return Stream.of(
                Arguments.of(LabelUtils.defaults, null),
                Arguments.of(Map.of(AUTHX_CERTIFICATE_DURATION, "xxx"), DataProcessingException.class),
                Arguments.of(Map.of(AUTHX_CERTIFICATE_DURATION, "123456"), null),
                Arguments.of(Map.of(AUTHX_LOGIN_ENABLED, "TRUE"), null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersForMerge")
    void testMergeWithDefaults(Map<String, String> labels, Map<String, String> labelsMerged) {
        Map<String, String> result = LabelUtils.mergeWithDefaults(labels);
        assertEquals(result.keySet(), labelsMerged.keySet());
    }

    @ParameterizedTest
    @MethodSource("provideParametersForTest")
    void testValidate(Map<String, String> labels, Class<Exception> exceptionType) {
        if (exceptionType == null) {
            assertDoesNotThrow(() -> LabelUtils.validate(labels));
        } else {
            assertThrows(exceptionType, () -> LabelUtils.validate(labels));
        }
    }

}
