package one.microproject.authx.jredis.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest()
@SpringBootApplication(scanBasePackages = {
        "one.microproject.authx.jredis.impl",
        "one.microproject.authx.jredis.model",
})
@EnableRedisRepositories(basePackages = {
        "one.microproject.authx.jredis.repository"
})
@ContextConfiguration(initializers = AppBaseTest.Initializer.class, classes = {
        one.microproject.authx.jredis.impl.TokenCacheReaderServiceImpl.class,
        one.microproject.authx.jredis.impl.TokenCacheWriterServiceImpl.class,
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public abstract class AppBaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(AppBaseTest.class);

    private static final int DOCKER_EXPOSED_REDIS_PORT = 6379;

    private static final String REDIS_DOCKER_IMAGE = "redis:6.2.6-alpine";

    private static GenericContainer<?> redisContainer;

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private final Logger LOGGER = LoggerFactory.getLogger(AppBaseTest.class);

        private static GenericContainer<?> redisContainer = new GenericContainer<>(REDIS_DOCKER_IMAGE)
                .withExposedPorts(DOCKER_EXPOSED_REDIS_PORT)
                .withReuse(true);

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext context) {

            redisContainer.start();
            Assertions.assertTrue(redisContainer.isRunning());
            AppBaseTest.redisContainer = redisContainer;
            Integer redisBoundPort = redisContainer.getMappedPort(DOCKER_EXPOSED_REDIS_PORT);

            LOGGER.info("REDIS      : {}:{}", redisContainer.getContainerIpAddress(), redisBoundPort);

            TestPropertyValues.of(
                    "spring.redis.host=" + redisContainer.getContainerIpAddress(),
                    "spring.redis.port=" + redisBoundPort
            ).applyTo(context.getEnvironment());
        }
    }

}
