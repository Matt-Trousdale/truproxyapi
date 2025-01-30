package uk.co.cloudmatica.truproxyapi.repo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
@Builder
public class Address {
    private String locality;
    @JsonProperty("postal_code")
    private String postalCode;
    @JsonProperty("premises")
    private String premises;
    @JsonProperty("address_line_1")
    private String addressLine1;
    @JsonProperty("country")
    private String country;
}
