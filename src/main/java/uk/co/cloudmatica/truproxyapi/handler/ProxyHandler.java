package uk.co.cloudmatica.truproxyapi.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import uk.co.cloudmatica.truproxyapi.dto.CompanyDto;
import uk.co.cloudmatica.truproxyapi.service.ProxyService;

import static reactor.core.publisher.Mono.just;

@RequiredArgsConstructor
public class ProxyHandler {

    final ProxyService proxyService;

    public Mono<ServerResponse> proxy(final ServerRequest serverRequest) {

        return serverRequest
            .bodyToMono(QueryFields.class).cache()
            .onErrorResume(throwable -> just(QueryFields.builder().build()))
            .transform(this::buildResponse)
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    private Mono<ServerResponse> buildResponse(final Mono<QueryFields> queryFields) {
        return queryFields
            .transform(proxyService::getCompany)
            .transform(this::serverResponse);
    }

    private Mono<ServerResponse> serverResponse(final Mono<CompanyDto> companyDtoMono) {
        return companyDtoMono.flatMap(appResponse ->
            ServerResponse.ok().body(just(appResponse), CompanyDto.class));
    }
}
