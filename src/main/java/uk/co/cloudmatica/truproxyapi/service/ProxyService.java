package uk.co.cloudmatica.truproxyapi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import uk.co.cloudmatica.truproxyapi.dto.CompanyDto;
import uk.co.cloudmatica.truproxyapi.handler.QueryFields;
import uk.co.cloudmatica.truproxyapi.repo.CompanyRepoRemote;
import uk.co.cloudmatica.truproxyapi.repo.model.CompanyHolder;
import uk.co.cloudmatica.truproxyapi.repo.model.OfficeHolder;

import java.util.function.Function;

@Slf4j
@AllArgsConstructor
public class ProxyService {

    private final CompanyRepoRemote companyRepoRemote;

    public Mono<CompanyDto> getCompany(final Mono<QueryFields> requestParams) {

        return companyRepoRemote.findCompanies(requestParams)
            .zipWith(companyRepoRemote.findOfficers(requestParams))
            .map(getSingleCompaniesWithOfficersEmbedded());
    }

    private static Function<Tuple2<CompanyHolder, OfficeHolder>, CompanyDto> getSingleCompaniesWithOfficersEmbedded() {

        return z -> {
            z.getT1().getCompanies().getFirst().setOfficers(z.getT2().getItems());
            return CompanyDto.builder()
                .totalResults(z.getT1().getTotalResults())
                .companies(z.getT1().getCompanies()).companies(z.getT1().getCompanies()).build();
        };
    }
}
