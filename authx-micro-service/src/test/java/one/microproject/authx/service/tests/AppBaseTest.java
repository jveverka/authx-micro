package one.microproject.authx.service.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = AppBaseTest.Initializer.class)
public abstract class AppBaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(AppBaseTest.class);

    private static final int DOCKER_EXPOSED_MONGO_PORT = 27017;
    private static final String MONGO_DOCKER_IMAGE = "mongo:4.4.4-bionic";
    private static MongoDBContainer mongoDBContainer;

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private final Logger LOGGER = LoggerFactory.getLogger(AppBaseTest.class);

        private static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGO_DOCKER_IMAGE);

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext context) {

            mongoDBContainer.start();
            Assertions.assertTrue(mongoDBContainer.isRunning());
            AppBaseTest.mongoDBContainer = mongoDBContainer;
            Integer boundPort = mongoDBContainer.getMappedPort(DOCKER_EXPOSED_MONGO_PORT);

            LOGGER.info("MONGO      : {}", mongoDBContainer.getReplicaSetUrl());
            LOGGER.info("MONGO      : mongodb://localhost:{}/test", boundPort);

            TestPropertyValues.of(
                    "spring.data.mongodb.host=localhost",
                    "spring.data.mongodb.port=" + boundPort,
                    "spring.data.mongodb.database=test"
            ).applyTo(context.getEnvironment());
        }
    }

}
