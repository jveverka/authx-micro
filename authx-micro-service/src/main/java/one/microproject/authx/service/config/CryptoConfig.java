package one.microproject.authx.service.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.Security;

@Configuration
public class CryptoConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoConfig.class);

    @PostConstruct
    private void init() {
        LOG.info("## CONFIG: initializing Bouncy Castle Provider (BCP) ...");
        Security.addProvider(new BouncyCastleProvider());
        LOG.info("## CONFIG: BCP initialized.");
    }

}
