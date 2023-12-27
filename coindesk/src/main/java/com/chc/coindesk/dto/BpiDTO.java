package com.chc.coindesk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BpiDTO {
    @Schema(
            description = "Currency code",
            name = "code",
            type = "string",
            example = "ABC"
    )
    @NotEmpty(message = "Code may not be empty")
    private String code;

    @Schema(
            description = "Currency symbol",
            name = "symbol",
            type = "string",
            example = "&#38;"
    )
    @NotEmpty
    private String symbol;

    @Schema(
            description = "Rate in string format",
            name = "rate",
            type = "string",
            example = "43,264.2424"
    )
    @NotEmpty
    private String rate;

    @Schema(
            description = "Currency description",
            name = "description",
            type = "string",
            example = "ABC Dollar"
    )
    @NotEmpty
    private String description;

    @Schema(
            description = "Rate in float type",
            name = "rate_float",
            type = "double",
            example = "43264.2424"
    )
    @NotEmpty
    private double rate_float;
}
