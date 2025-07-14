package uk.co.cloudmatica.truproxyapi.repo;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.co.cloudmatica.truproxyapi.handler.QueryFields;
import uk.co.cloudmatica.truproxyapi.repo.model.CompanyHolder;
import uk.co.cloudmatica.truproxyapi.repo.model.OfficeHolder;

import java.util.function.Function;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

public class CompanyRepoRemote {

    private static final String COMPANY_RESOURCE = "/Search?";
    private static final String OFFICER_RESOURCE = "/Officers?";
    private static final String CUSTOMER_SEARCH = "CompanyNumber=";
    private static final String QUERY_PREFIX = "Query=";
    private final String url;
    private final WebClient webClient;

    public CompanyRepoRemote(final String url, WebClient webClient) {

        this.url = url;
        this.webClient = webClient;
    }

    public Mono<CompanyHolder> findCompanies(final Mono<QueryFields> query) {

        return query
            .transform(s -> buildUrl(s, COMPANY_RESOURCE, QUERY_PREFIX))
            .transform(s -> get(s, r -> r.bodyToMono(CompanyHolder.class)));
    }

    public Mono<OfficeHolder> findOfficers(final Mono<QueryFields> query) {
        return query
            .transform(s -> buildUrl(s , OFFICER_RESOURCE, CUSTOMER_SEARCH))
            .transform(e -> get(e, r -> r.bodyToMono(OfficeHolder.class)));
    }

    private Mono<String> buildUrl(final Mono<QueryFields> query, String resource, String nameOfIdField) {
        return query.flatMap(companyNumber -> just(url.concat(resource)
            .concat(nameOfIdField)
            .concat(companyNumber.getCompanyNumber()))
        );
    }

    private <E> Mono<E> get(final Mono<String> urlMono, Function<ClientResponse, Mono<E>> function) {
        return urlMono.flatMap(url -> webClient
            .get()
            .uri(url)
            .accept(APPLICATION_JSON)
            .exchangeToMono(function));
    }
}
