package uk.co.cloudmatica.truproxyapi.repo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
@Builder
@AllArgsConstructor
@Getter
public class OfficeHolder {

    private List<Officer> items;
}

