package one.microproject.authx.service.model;

import one.microproject.authx.common.dto.KeyPairSerialized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
public class Client {

    @Id
    private String id;

    private String clientId;
    private String projectId;
    private String description;
    private String secret;
    private String defaultKid;
    private Map<String, String> labels;
    private Map<String, KeyPairSerialized> keyPairs;
    private List<String> groups;
    private List<String> roles;

    public Client() {
    }

    public Client(String id, String clientId, String projectId, String description, String secret,
                  String defaultKid, Map<String, String> labels, Map<String, KeyPairSerialized> keyPairs,
                  List<String> groups, List<String> roles) {
        this.id = id;
        this.clientId = clientId;
        this.projectId = projectId;
        this.description = description;
        this.secret = secret;
        this.defaultKid = defaultKid;
        this.labels = labels;
        this.keyPairs = keyPairs;
        this.groups = groups;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
