package com.chc.coindesk.dto;

import lombok.Data;

@Data
public class BpiDTO {
    String code;
    String symbol;
    String rate;
    String description;
    double rate_float;
}
