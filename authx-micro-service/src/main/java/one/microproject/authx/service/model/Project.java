package one.microproject.authx.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
public class Project {

    @Id
    private String id;

    private String description;
    private List<String> adminIds;
    private Map<String, String> labels;

    public Project() {
    }

    public Project(String id, String description, List<String> adminIds, Map<String, String> labels) {
        this.id = id;
        this.description = description;
        this.adminIds = adminIds;
        this.labels = labels;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public List<String> getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(List<String> adminIds) {
        this.adminIds = adminIds;
    }

}
