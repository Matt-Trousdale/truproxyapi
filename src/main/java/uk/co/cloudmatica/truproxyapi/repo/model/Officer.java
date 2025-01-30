package uk.co.cloudmatica.truproxyapi.repo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
@Builder
public class Officer {

    @JsonProperty("name")
    private String name;
    @JsonProperty("officer_role")
    private String officerRole;
    @JsonProperty("appointed_on")
    private String appointedOn;
    @JsonProperty("address")
    private Address address;
}
