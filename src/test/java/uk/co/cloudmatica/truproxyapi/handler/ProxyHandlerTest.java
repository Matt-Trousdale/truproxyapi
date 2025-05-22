package uk.co.cloudmatica.truproxyapi.handler;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.test.StepVerifier;
import uk.co.cloudmatica.truproxyapi.dto.CompanyDto;
import uk.co.cloudmatica.truproxyapi.service.ProxyService;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.seeOther;
import static reactor.core.publisher.Mono.*;

@ExtendWith(MockitoExtension.class)
class ProxyHandlerTest {

    @InjectMocks
    private ProxyHandler underTest;
    @Mock
    ProxyService proxyServiceMock;
    @Mock
    private ServerRequest serverRequestMock;

    @Test
    void givenInputTheHandleShouldReturnResponse() {

        final var queryFiledsMono = just(QueryFields.builder()
            .companyNumber("1234").build());
        final var companyMono = just(CompanyDto.builder().build());

        given(serverRequestMock.bodyToMono(QueryFields.class)).willReturn(queryFiledsMono);
        given(proxyServiceMock.getCompany(any())).willReturn(companyMono);

        StepVerifier
            .create(underTest.proxy(serverRequestMock))
            .assertNext(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
            .verifyComplete();
    }

    @Test
    void givenNoInputTheHandleShouldReturnResponse() {

        given(serverRequestMock.bodyToMono(QueryFields.class)).willReturn(empty());
        given(proxyServiceMock.getCompany(any())).willReturn(empty());

        StepVerifier
            .create(underTest.proxy(serverRequestMock))
            .consumeNextWith(serverResponse -> assertEquals(NOT_FOUND, serverResponse.statusCode()))
            .verifyComplete();
    }
}