package one.microproject.authx.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class AuthxApp {

    public static void main(String[] args) {
        SpringApplication.run(AuthxApp.class, args);
    }

}
