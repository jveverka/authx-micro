package one.microproject.authx.service.tests.service;

import one.microproject.authx.common.dto.CreateUserRequest;
import one.microproject.authx.common.dto.UserDto;
import one.microproject.authx.service.service.UserService;
import one.microproject.authx.service.tests.AppBaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest extends AppBaseTest {

    @Autowired
    UserService userService;

    @Test
    void testUserAddRemove() {
        String projectId = "p-01";
        String clientId = "c-01";
        CreateUserRequest request = new CreateUserRequest("u-01", "user@email.com", "d", "s01", Map.of());
        UserDto userDto = userService.create(projectId, clientId, request);
        assertNotNull(userDto);
        assertEquals(request.id(), userDto.id());

        List<UserDto> userDtos = userService.getAll(projectId);
        assertEquals(1, userDtos.size());

        assertTrue(userService.verifySecret(projectId, "u-01", "s01"));
        assertFalse(userService.verifySecret(projectId, "u-01", "xxx"));

        userService.setSecret(projectId, "u-01", "zzz");

        assertTrue(userService.verifySecret(projectId, "u-01", "zzz"));
        assertFalse(userService.verifySecret(projectId, "u-01", "xxx"));
    }

    @AfterEach
    private void cleanup() {
        userService.removeAll();
    }

}
