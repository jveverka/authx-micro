package one.microproject.authx.service.service.impl;

import one.microproject.authx.service.service.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

}
