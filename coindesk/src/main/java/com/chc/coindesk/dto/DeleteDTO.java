package com.chc.coindesk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteDTO {
    @Schema(
            description = "Currency code",
            name = "code",
            type = "string",
            example = "ABC"
    )
    private String code;
}
