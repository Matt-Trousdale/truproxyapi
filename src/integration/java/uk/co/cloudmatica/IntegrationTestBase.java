package uk.co.cloudmatica;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import uk.co.cloudmatica.truproxyapi.config.AppConfig;
import uk.co.cloudmatica.truproxyapi.config.WebConfig;

import java.io.File;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@EnableAutoConfiguration
@ContextConfiguration(classes = {WebConfig.class, AppConfig.class})
@AutoConfigureWebTestClient
@Profile("test")
public abstract class IntegrationTestBase {

    @Container
    public static DockerComposeContainer dockerComposeContainer =
        new DockerComposeContainer(new File("src/integration/mock-server/docker-compose.yml"))
            .withExposedService("mockServer", 1080);

    @ServiceConnection
    final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");

    public IntegrationTestBase() {
        dockerComposeContainer.start();
        mongoDBContainer.start();
    }
}
