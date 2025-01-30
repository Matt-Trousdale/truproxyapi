package uk.co.cloudmatica.truproxyapi.repo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
@Builder
@Getter
@AllArgsConstructor
public class CompanyHolder {

    @JsonProperty("total_results")
    private Integer totalResults;
    @JsonProperty("items")
    private List<Company> companies;
}
