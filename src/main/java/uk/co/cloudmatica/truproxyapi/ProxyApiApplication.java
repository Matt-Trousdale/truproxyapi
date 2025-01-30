package uk.co.cloudmatica.truproxyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableReactiveMongoRepositories
public class ProxyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyApiApplication.class, args);
    }
}
