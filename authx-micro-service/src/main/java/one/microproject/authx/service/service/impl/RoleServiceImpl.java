package one.microproject.authx.service.service.impl;

import one.microproject.authx.service.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {
}
