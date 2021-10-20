package one.microproject.authx.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
public class Group {

    @Id
    private String id;
    private String projectId;
    private Map<String, String> labels;

}
