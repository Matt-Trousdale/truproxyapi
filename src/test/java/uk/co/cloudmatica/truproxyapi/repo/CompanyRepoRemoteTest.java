package uk.co.cloudmatica.truproxyapi.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import uk.co.cloudmatica.truproxyapi.handler.QueryFields;
import uk.co.cloudmatica.truproxyapi.repo.model.CompanyHolder;
import uk.co.cloudmatica.truproxyapi.repo.model.OfficeHolder;

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
class CompanyRepoRemoteTest {

    @Mock
    private WebClient webClientMock;
    @Mock
    private RequestBodyUriSpec requestBodyUriSpecMock;
    @Mock
    private RequestHeadersSpec<RequestBodySpec> requestHeadersSpecMock;
    @SuppressWarnings("rawtypes")
    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpecMock;
    private CompanyRepoRemote underTest;

    @BeforeEach
    void setUp() {

        underTest = new CompanyRepoRemote("http://test", webClientMock);
    }

    @SuppressWarnings("unchecked")
    @Test
    void findCompanies() {

        final var queryFiledsMono = just(QueryFields.builder().companyNumber("1234").build());
        final var companyMono = just(CompanyHolder.builder().build());

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.accept(APPLICATION_JSON)).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.exchangeToMono(
                ArgumentMatchers.<Function<ClientResponse, Mono<CompanyHolder>>>notNull())).thenReturn(companyMono);

        StepVerifier
                .create(underTest.findCompanies(queryFiledsMono))
                .assertNext(c -> assertThat(c, equalTo(companyMono.block())))
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void findOfficers() {

        final var queryFiledsMono = just(QueryFields.builder()
                .companyNumber("1234").build());
        final var officeMono = just(OfficeHolder.builder()
                .build());

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.accept(APPLICATION_JSON)).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.exchangeToMono(
                ArgumentMatchers.<Function<ClientResponse, Mono<OfficeHolder>>>notNull())).thenReturn(officeMono);

        StepVerifier
                .create(underTest.findOfficers(queryFiledsMono))
                .assertNext(c -> assertThat(c, equalTo(officeMono.block())))
                .verifyComplete();
    }

}