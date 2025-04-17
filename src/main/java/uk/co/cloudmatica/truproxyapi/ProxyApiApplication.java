package uk.co.cloudmatica.truproxyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ProxyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyApiApplication.class, args);
    }
}
