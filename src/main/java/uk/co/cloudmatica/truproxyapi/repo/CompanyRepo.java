package uk.co.cloudmatica.truproxyapi.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import uk.co.cloudmatica.truproxyapi.repo.model.Company;

public interface CompanyRepo extends ReactiveMongoRepository<Company, Long> {

    Mono<Company> findByCompanyNumber(final String companyNumber);

    Mono<Company> findByTitle(final String companyNumber);
}
