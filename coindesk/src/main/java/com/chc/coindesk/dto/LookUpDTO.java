package com.chc.coindesk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LookUpDTO {
    @Schema(
            description = "Currency code",
            name = "code",
            type = "string",
            example = "USA"
    )
    private String code;

    @Schema(
            description = "Language Id ex: 1:English 2:Chinese",
            name = "language_id",
            type = "integer",
            example = "1"
    )
    private int language_id;
}
