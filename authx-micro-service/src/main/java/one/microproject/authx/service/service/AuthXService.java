package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.AuthxDto;

import java.util.Optional;

public interface AuthXService {

    Optional<AuthxDto> getAuthxInfo();

    void createOrUpdate(AuthxDto authxInfo);

}
