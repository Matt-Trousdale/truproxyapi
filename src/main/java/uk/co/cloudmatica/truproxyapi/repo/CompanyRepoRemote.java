package uk.co.cloudmatica.truproxyapi.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.co.cloudmatica.truproxyapi.handler.QueryFields;
import uk.co.cloudmatica.truproxyapi.repo.model.CompanyHolder;
import uk.co.cloudmatica.truproxyapi.repo.model.OfficeHolder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@Slf4j
public class CompanyRepoRemote {

    private static final String COMPANY_RESOURCE = "/Search?";
    private static final String OFFICER_RESOURCE = "/Officers?";
    private static final String CUSTOMER_SEARCH = "CompanyNumber=";
    private final String url;
    private final WebClient webClient;

    public CompanyRepoRemote(final String url, WebClient webClient) {

        this.url = url;
        this.webClient = webClient;
    }

    public Mono<CompanyHolder> findCompanies(final Mono<QueryFields> query) {

        return query
            .transform(this::buildCompUrl)
            .transform(this::get);
    }

    public Mono<OfficeHolder> findOfficers(final Mono<QueryFields> query) {
        return query
            .transform(this::buildOfficerUrl)
            .transform(this::getOffices);
    }

    private Mono<String> buildCompUrl(final Mono<QueryFields> query) {
        return query.flatMap(companyNumber -> {
                log.info(companyNumber.toString());
                return just(url.concat(COMPANY_RESOURCE).concat("Query=").concat(companyNumber.getCompanyNumber()));
            }
        );
    }

    private Mono<String> buildOfficerUrl(final Mono<QueryFields> query) {
        return query.flatMap(companyNumber -> {
                log.info(companyNumber.toString());
                return just(url.concat(OFFICER_RESOURCE)
                    .concat(CUSTOMER_SEARCH)
                    .concat(companyNumber.getCompanyNumber()));
            }
        );
    }

    private Mono<OfficeHolder> getOffices(final Mono<String> urlMono) {
        return urlMono.flatMap(url -> webClient
            .get()
            .uri(url)
            .accept(APPLICATION_JSON)
            .exchangeToMono(e -> e.bodyToMono(OfficeHolder.class)));
    }

    private Mono<CompanyHolder> get(final Mono<String> urlMono) {
        return urlMono.flatMap(url -> webClient
            .get()
            .uri(url)
            .accept(APPLICATION_JSON)
            .exchangeToMono(e -> e.bodyToMono(CompanyHolder.class)));
    }
}
