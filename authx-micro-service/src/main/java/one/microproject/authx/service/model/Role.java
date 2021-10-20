package one.microproject.authx.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Role {

    @Id
    private String id;

    private String projectId;
    private String description;
    private List<String> permissionIds;

}
