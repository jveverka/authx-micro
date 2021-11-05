package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.AuthxDto;
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

    private final AuthxRepository authxRepository;

    @Autowired
    public AuthXServiceImpl(AuthxRepository authxRepository) {
        this.authxRepository = authxRepository;
    }

    @Override
    public Optional<Authx> getAuthxInfo() {
        return authxRepository.findAll().stream().findFirst();
    }

    @Override
    @Transactional
    public void create(AuthxDto authxDto, String globalAdminProjectId) {
        authxRepository.deleteAll();
        Authx authx = new Authx(authxDto.id(), globalAdminProjectId);
        authxRepository.save(authx);
    }

}
