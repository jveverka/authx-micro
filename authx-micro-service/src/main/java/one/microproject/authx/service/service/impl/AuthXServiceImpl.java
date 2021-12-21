package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.AuthxDto;
import one.microproject.authx.common.dto.AuthxInfo;
import one.microproject.authx.service.model.Authx;
import one.microproject.authx.service.repository.AuthxRepository;
import one.microproject.authx.service.service.AuthXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthXServiceImpl implements AuthXService {

    private final DMapper dMapper;
    private final AuthxRepository authxRepository;

    @Autowired
    public AuthXServiceImpl(DMapper dMapper, AuthxRepository authxRepository) {
        this.dMapper = dMapper;
        this.authxRepository = authxRepository;
    }

    @Override
    public Optional<AuthxDto> getAuthxInfo() {
        return authxRepository.findAll().stream().map(dMapper::map).findFirst();
    }

    @Override
    @Transactional
    public void createOrUpdate(AuthxDto authxInfo) {
        authxRepository.deleteAll();
        Authx authx = new Authx(authxInfo.id(), authxInfo.globalAdminProjectIds());
        authxRepository.save(authx);
    }

}
