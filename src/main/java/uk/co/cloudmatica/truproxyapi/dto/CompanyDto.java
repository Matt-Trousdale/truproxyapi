package uk.co.cloudmatica.truproxyapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import uk.co.cloudmatica.truproxyapi.repo.model.Company;

import java.util.List;

@Builder
public class CompanyDto {

    @JsonProperty("total_results")
    final Integer totalResults;
    @Getter
    @JsonProperty("items")
    private List<Company> companies;
}
