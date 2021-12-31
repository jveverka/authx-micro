package one.microproject.authx.service.tests;

import one.microproject.authx.common.dto.*;
import one.microproject.authx.common.dto.oauth2.TokenResponse;
import one.microproject.authx.jclient.AuthXClient;
import one.microproject.authx.jclient.AuthXClientBuilder;
import one.microproject.authx.jclient.AuthXOAuth2Client;
import one.microproject.authx.jclient.impl.AuthXClientException;
import one.microproject.authx.service.service.DataInitService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = AppBaseTest.Initializer.class)
public abstract class AppBaseTest {

    @Autowired
    private DataInitService dataInitService;

    private AuthXClient adminClient;
    private AuthXOAuth2Client globalAdminClient;
    private String baseUrl;

    @LocalServerPort
    private int port;

    @PostConstruct
    public void init() {
        baseUrl = "http://localhost:" + port + "/authx";
        adminClient = new AuthXClientBuilder()
                .withBaseUrl(baseUrl)
                .build();
        globalAdminClient = adminClient.getAuthXOAuth2Client(dataInitService.getGlobalAdminProjectIds().get(0));
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public AuthXClient getAuthXClient() {
        return adminClient;
    }

    public AuthXOAuth2Client getGlobalAdminOAuth2Client() {
        return globalAdminClient;
    }

    public TokenResponse getGlobalAdminTokens() {
        ClientCredentials clientCredentials = new ClientCredentials("admin-client", "secret");
        Set<String> scopes = Set.of();
        UserCredentials userCredentials = new UserCredentials("admin-user", "s3cr3t");
        AuthXResponse<TokenResponse, Void> authXResponse = globalAdminClient.getTokenForPassword(clientCredentials, "", scopes, userCredentials);
        if (authXResponse.isSuccess()) {
            return authXResponse.response();
        } else {
            throw new AuthXClientException();
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(AppBaseTest.class);

    private static final int DOCKER_EXPOSED_MONGO_PORT = 27017;
    private static final int DOCKER_EXPOSED_REDIS_PORT = 6379;

    private static final String MONGO_DOCKER_IMAGE = "mongo:5.0.5-focal";
    private static final String REDIS_DOCKER_IMAGE = "redis:6.2.6-alpine";

    private static MongoDBContainer mongoDBContainer;
    private static GenericContainer<?> redisContainer;

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private final Logger LOGGER = LoggerFactory.getLogger(AppBaseTest.class);

        private static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGO_DOCKER_IMAGE)
                .withExposedPorts(DOCKER_EXPOSED_MONGO_PORT)
                .withReuse(true);
        private static GenericContainer<?> redisContainer = new GenericContainer<>(REDIS_DOCKER_IMAGE)
                .withExposedPorts(DOCKER_EXPOSED_REDIS_PORT)
                .withReuse(true);

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext context) {

            redisContainer.start();
            Assertions.assertTrue(redisContainer.isRunning());
            AppBaseTest.redisContainer = redisContainer;
            Integer redisBoundPort = redisContainer.getMappedPort(DOCKER_EXPOSED_REDIS_PORT);

            mongoDBContainer.start();
            Assertions.assertTrue(mongoDBContainer.isRunning());
            AppBaseTest.mongoDBContainer = mongoDBContainer;
            Integer mongoBoundPort = mongoDBContainer.getMappedPort(DOCKER_EXPOSED_MONGO_PORT);

            LOGGER.info("MONGO      : {}", mongoDBContainer.getReplicaSetUrl());
            LOGGER.info("MONGO      : mongodb://localhost:{}/test", mongoBoundPort);
            LOGGER.info("REDIS      : {}:{}", redisContainer.getContainerIpAddress(), redisBoundPort);

            TestPropertyValues.of(
                    "spring.data.mongodb.host=localhost",
                    "spring.data.mongodb.port=" + mongoBoundPort,
                    "spring.redis.host=" + redisContainer.getContainerIpAddress(),
                    "spring.redis.port=" + redisBoundPort
            ).applyTo(context.getEnvironment());
        }
    }

    public BuildProjectRequest createBuildProjectRequest(String pid) {
        CreateUserRequest adminUser = new CreateUserRequest("admin-user-001", "admin@pid-001", "", "secret", Map.of(), Set.of(), Set.of(), "admin-client-001");
        CreateClientRequest adminClient = new CreateClientRequest("admin-client-001", "", false, "secret", Map.of(), Set.of(), Set.of());
        CreateProjectRequest cp = new CreateProjectRequest(pid, "desc", Map.of(), adminUser, adminClient);
        List<GroupDto> groups = List.of(new GroupDto("g-001", "description", Map.of()));
        List<PermissionDto> permissions = List.of(new PermissionDto("p-001", "description", "service", "resource", "action"));
        List<RoleDto> roles = List.of(new RoleDto("r-001", "description", Set.of("p-001")));
        CreateClientRequest normalClient = new CreateClientRequest("cl-001", "", true, "secret", Map.of(), Set.of("r-001"), Set.of("g-001"));
        CreateUserRequest normalUser = new CreateUserRequest("u-001", "u@p-001", "", "secret", Map.of(), Set.of("r-001"), Set.of("g-001"), "cl-001");
        return new BuildProjectRequest(cp, groups, roles, permissions, List.of(normalClient), List.of(normalUser));
    }

}
