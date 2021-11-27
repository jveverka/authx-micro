package one.microproject.authx.service.service.impl;

import one.microproject.authx.common.dto.KeyPairData;
import one.microproject.authx.common.utils.CryptoUtils;
import one.microproject.authx.service.service.CryptoService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Override
    public KeyPairData generateKeyPair(String id, String subject, TimeUnit unit, Long duration) {
        Instant notBefore = Instant.now();
        return CryptoUtils.generateSelfSignedKeyPair(id, subject, notBefore, unit, duration);
    }

}
