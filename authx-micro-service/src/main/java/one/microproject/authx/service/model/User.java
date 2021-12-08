package one.microproject.authx.service.model;

import one.microproject.authx.common.dto.KeyPairSerialized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
public class User {

    @Id
    private String id;

    private String userId;
    private String projectId;
    private String clientId;
    private String email;
    private String description;
    private String secret;
    private String defaultKid;
    private Map<String, String> labels;
    private Map<String, KeyPairSerialized> keyPairs;

    public User() {
    }

    public User(String id, String userId, String projectId, String clientId, String email, String description, String secret,
                String defaultKid, Map<String, String> labels, Map<String, KeyPairSerialized> keyPairs) {
        this.id = id;
        this.userId = userId;
        this.projectId = projectId;
        this.clientId = clientId;
        this.email = email;
        this.description = description;
        this.secret = secret;
        this.defaultKid = defaultKid;
        this.labels = labels;
        this.keyPairs = keyPairs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDefaultKid() {
        return defaultKid;
    }

    public void setDefaultKid(String defaultKid) {
        this.defaultKid = defaultKid;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public Map<String, KeyPairSerialized> getKeyPairs() {
        return keyPairs;
    }

    public void setKeyPairs(Map<String, KeyPairSerialized> keyPairs) {
        this.keyPairs = keyPairs;
    }

}
