package one.microproject.authx.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Authx {

    @Id
    private String id;

    private String globalAdminProjectId;

    public Authx() {
    }

    public Authx(String id, String globalAdminProjectId) {
        this.id = id;
        this.globalAdminProjectId = globalAdminProjectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlobalAdminProjectId() {
        return globalAdminProjectId;
    }

    public void setGlobalAdminProjectId(String globalAdminProjectId) {
        this.globalAdminProjectId = globalAdminProjectId;
    }

}
