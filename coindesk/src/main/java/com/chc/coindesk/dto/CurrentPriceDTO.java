package com.chc.coindesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentPriceDTO {
    private Map<String, String> time;
    private String disclaimer;
    private String chartName;
    private Map<String, BpiDTO> bpi;
}
