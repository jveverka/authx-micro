package one.microproject.authx.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Authx {

    @Id
    private String id;

    private List<String> globalAdminProjectIds;

    public Authx() {
    }

    public Authx(String id, List<String> globalAdminProjectIds) {
        this.id = id;
        this.globalAdminProjectIds = globalAdminProjectIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGlobalAdminProjectIds() {
        return globalAdminProjectIds;
    }

    public void setGlobalAdminProjectIds(List<String> globalAdminProjectIds) {
        this.globalAdminProjectIds = globalAdminProjectIds;
    }

}
