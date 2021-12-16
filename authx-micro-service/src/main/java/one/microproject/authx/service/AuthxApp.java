package one.microproject.authx.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@SpringBootApplication(scanBasePackages = {
        "one.microproject.authx.jredis.impl",
        "one.microproject.authx.jredis.model",
        "one.microproject.authx.service"
})
@EnableRedisRepositories(basePackages = {
        "one.microproject.authx.jredis.repository"
})
public class AuthxApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthxApp.class, args);
    }

}
