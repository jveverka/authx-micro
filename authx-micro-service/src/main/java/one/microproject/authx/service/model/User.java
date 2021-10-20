package one.microproject.authx.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
public class User {

    @Id
    private String id;

    private String projectId;
    private String clientId;
    private String email;
    private String description;
    private String secret;
    private Map<String, String> labels;

}
