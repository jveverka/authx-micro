package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.AuthxDto;
import one.microproject.authx.service.model.Authx;

import java.util.Optional;

public interface AuthXService {

    Optional<Authx> getAuthxInfo();

    void create(AuthxDto authxDto, String globalAdminProjectId);

}
