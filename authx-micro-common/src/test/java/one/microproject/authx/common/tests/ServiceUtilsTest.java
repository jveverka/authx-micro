package one.microproject.authx.common.tests;


import one.microproject.authx.common.dto.PermissionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static one.microproject.authx.common.utils.ServiceUtils.createId;
import static one.microproject.authx.common.utils.ServiceUtils.getScopes;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceUtilsTest {

    private final static Set<PermissionDto> p1 = Set.of(
            new PermissionDto("p1", "d1", "s", "r", "a1")
    );

    private final static Set<PermissionDto> p2 = Set.of(
            new PermissionDto("p1", "d1", "s", "r", "a1"),
            new PermissionDto("p1", "d1", "s", "r", "a2")
    );

    @Test
    void testCreateId() {
        String id = createId("project-id", "id");
        assertEquals("project-id-id", id);
    }

    private static Stream<Arguments> provideForGetScopes() {
        return Stream.of(
                Arguments.of(Set.of(), Set.of(), Set.of()),
                Arguments.of(Set.of("r.s.a1"), Set.of(), Set.of()),
                Arguments.of(Set.of("r.s.a1", "r.s.a2"), Set.of(), Set.of()),
                Arguments.of(Set.of(), p1, Set.of("r.s.a1")),
                Arguments.of(Set.of(), p2, Set.of("r.s.a1", "r.s.a2")),
                Arguments.of(Set.of("r.s.a1"), p2, Set.of("r.s.a1")),
                Arguments.of(Set.of("r.s.a2"), p2, Set.of("r.s.a2")),
                Arguments.of(Set.of("r.s.a1"), p1, Set.of("r.s.a1"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideForGetScopes")
    void testGetScopes(Set<String> requestedScopes, Set<PermissionDto> permissions, Set<String> expectedResult) {
        Set<String> result = getScopes(requestedScopes, permissions);
        assertEquals(expectedResult, result);
    }

}
