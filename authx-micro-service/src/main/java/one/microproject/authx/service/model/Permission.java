package one.microproject.authx.service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
public class Permission {

    @Id
    private String id;

    private String projectId;
    private String description;
    private String service;
    private String resource;
    private String action;

}
