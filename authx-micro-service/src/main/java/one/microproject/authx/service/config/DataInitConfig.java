package one.microproject.authx.service.config;

import one.microproject.authx.common.dto.AuthxDto;
import one.microproject.authx.common.dto.CreateClientRequest;
import one.microproject.authx.common.dto.CreateProjectRequest;
import one.microproject.authx.common.dto.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class DataInitConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitConfig.class);

    @Value("${authx.app-id}")
    private String appId;

    @Value("${authx.global-admins.project-name}")
    private String projectName;
    @Value("${authx.global-admins.admin-user.username}")
    private String adminUser;
    @Value("${authx.global-admins.admin-user.password}")
    private String adminPassword;
    @Value("${authx.global-admins.admin-client.client-id}")
    private String clientId;
    @Value("${authx.global-admins.admin-client.client-secret}")
    private String clientSecret;

    @Bean
    public CreateProjectRequest getInitialModel() {
        LOGGER.info("##CONFIG: Global Admins");
        LOGGER.info("##CONFIG:  Project: {}", projectName);
        LOGGER.info("##CONFIG:    Admin: {}", adminUser);
        LOGGER.info("##CONFIG:    Client: {}", clientId);
        CreateUserRequest adminUserRequest = new CreateUserRequest(adminUser, "", "Global Admin", adminPassword, Map.of());
        CreateClientRequest adminClientRequest = new CreateClientRequest(clientId, "Global Admin Client", false, clientSecret, Map.of());
        return new CreateProjectRequest(projectName, "Global Admins", Map.of(), adminUserRequest, adminClientRequest);
    }

    @Bean
    public AuthxDto getApplicatitionId() {
        return new AuthxDto(appId);
    }

}
