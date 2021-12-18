package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ResponseMessage;

public interface AdminService {

    ResponseMessage buildProject(BuildProjectRequest buildProjectRequest);

    ResponseMessage deleteRecursively(String projectId);

}
