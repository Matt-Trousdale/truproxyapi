package uk.co.cloudmatica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import uk.co.cloudmatica.truproxyapi.dto.CompanyDto;
import uk.co.cloudmatica.truproxyapi.handler.ProxyHandler;
import uk.co.cloudmatica.truproxyapi.handler.QueryFields;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationIT extends IntegrationTestBase {

    @Autowired
    private RouterFunction<ServerResponse> route;
    @Autowired
    private ProxyHandler proxyHandler;
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

        CompanyDto companyDto = CompanyDto.builder().build();
        Mono<CompanyDto> companyDtoMono = Mono.just(companyDto);

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
}
