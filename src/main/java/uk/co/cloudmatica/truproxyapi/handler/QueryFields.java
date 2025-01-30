package uk.co.cloudmatica.truproxyapi.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class QueryFields {

    private String companyName;
    private String companyNumber;
}
