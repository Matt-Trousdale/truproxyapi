package uk.co.cloudmatica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import uk.co.cloudmatica.truproxyapi.dto.CompanyDto;
import uk.co.cloudmatica.truproxyapi.handler.QueryFields;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.core.publisher.Mono.empty;

public class ApplicationIT extends IntegrationTestBase {
    
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = webTestClient.mutate()
            .responseTimeout(Duration.ofMillis(40000))
            .build();
    }

    @Test
    void whenIAskForAnExistingCompanyItsReturnedWithOfficersIncluded() {

        webTestClient.post()
            .uri("/proxy")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromPublisher(Mono.just(QueryFields
                .builder()
                .companyName("BBC LIMITED")
                .companyNumber("06500244")
                .build()), QueryFields.class))
            .exchange()
            .expectStatus().isOk()
            .expectBody(CompanyDto.class)
            .value(response -> {
                assertThat(response).isNotNull();
                assertThat(response.getCompanies()).isNotNull();
                assertThat(response.getCompanies().getFirst().getCompanyNumber()).isEqualTo("06500244");
                assertThat(response.getCompanies().getFirst().getOfficers()).isNotNull();
                assertThat(response.getCompanies().getFirst().getOfficers().size()).isEqualTo(4);
            });

    }

    @Test
    void whenMissingQueryFieldsThenNotFound() {

        webTestClient.post()
            .uri("/proxy")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromPublisher(empty(), QueryFields.class))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(CompanyDto.class)
            .value(response -> assertThat(response).isNull());
    }
}
