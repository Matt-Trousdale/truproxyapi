package uk.co.cloudmatica.truproxyapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.cloudmatica.truproxyapi.handler.QueryFields;
import uk.co.cloudmatica.truproxyapi.repo.CompanyRepoRemote;
import uk.co.cloudmatica.truproxyapi.repo.model.Company;
import uk.co.cloudmatica.truproxyapi.repo.model.CompanyHolder;
import uk.co.cloudmatica.truproxyapi.repo.model.OfficeHolder;
import uk.co.cloudmatica.truproxyapi.repo.model.Officer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
class ProxyServiceTest {

    @InjectMocks
    private ProxyService underTest;

    @Mock
    CompanyRepoRemote companyRepoRemoteMock;

    @Test
    void whenTheServiceIsCalledItGoesToTheRepoAndGatherTheData() {

        given(companyRepoRemoteMock.findCompanies(any())).willReturn(
            just(CompanyHolder
                .builder()
                    .totalResults(1)
                    .companies(List.of(Company
                        .builder()
                            .title("cloudmatica ltd")
                            .companyNumber("1234")
                        .build()))
                .build()));

        final var expectedOfficer = Officer
            .builder()
            .name("javier")
            .build();

        given(companyRepoRemoteMock.findOfficers(any())).willReturn(
            just(OfficeHolder
                .builder()
                .items(List.of(expectedOfficer))
                .build())
        );

        final var actual = underTest.getCompany(just(QueryFields.builder().build())).block();

        assertThat(actual).isNotNull();
        assertThat(actual.getCompanies()).isNotNull();
        assertThat(actual.getCompanies().size()).isEqualTo(1);
        assertThat(actual.getCompanies().getFirst().getOfficers()).isNotNull();

        final var actualOfficer = actual.getCompanies().getFirst().getOfficers();

        assertThat(actualOfficer.size()).isEqualTo(1);
        assertThat(actual.getCompanies().getFirst().getOfficers().getFirst())
            .usingRecursiveComparison().isEqualTo(expectedOfficer);

    }
}