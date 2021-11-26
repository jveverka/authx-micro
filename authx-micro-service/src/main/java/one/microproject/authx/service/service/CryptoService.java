package one.microproject.authx.service.service;

import one.microproject.authx.common.dto.KeyPairData;

import java.util.concurrent.TimeUnit;

public interface CryptoService {

    KeyPairData generateKeyPair(String id, String subject, TimeUnit unit, Long duration);

}
