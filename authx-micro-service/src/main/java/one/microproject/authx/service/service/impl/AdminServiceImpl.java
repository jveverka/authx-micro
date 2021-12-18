package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.BuildProjectRequest;
import one.microproject.authx.common.dto.ResponseMessage;
import one.microproject.authx.service.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public ResponseMessage buildProject(BuildProjectRequest buildProjectRequest) {
        return null;
    }

    @Override
    public ResponseMessage deleteRecursively(String projectId) {
        return null;
    }

}
