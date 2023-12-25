package com.chc.coindesk.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class BpiDTO {

    @NotEmpty(message = "Code may not be empty")
    String code;

    @NotEmpty
    String symbol;
    @NotEmpty
    String rate;
    @NotEmpty
    String description;
    @NotEmpty
    double rate_float;
}
