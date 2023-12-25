package com.chc.coindesk.dto;

import lombok.Getter;

@Getter
public class BpiConvertedDTO {
    private final String code;
    private final String symbol;
    private final String rate;
    private final String description;
    private final double rate_float;

    private BpiConvertedDTO(Builder builder) {
        this.code = builder.code;
        this.symbol = builder.symbol;
        this.rate = builder.rate;
        this.description = builder.description;
        this.rate_float = builder.rate_float;
    }
    public static class Builder {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        private double rate_float;

        public Builder() {
            // You can set default values here if needed
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder rate(String rate) {
            this.rate = rate;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder rateFloat(double rate_float) {
            this.rate_float = rate_float;
            return this;
        }

        public BpiConvertedDTO build() {
            return new BpiConvertedDTO(this);
        }
    }
}

