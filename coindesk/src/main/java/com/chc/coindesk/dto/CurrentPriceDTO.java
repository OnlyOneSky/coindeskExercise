package com.chc.coindesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentPriceDTO {
    private Map<String, String> time;
    private String disclaimer;
    private String chartName;
    private Map<String, BpiDTO> bpi;

    public static class Builder {
        private Map<String, String> time;
        private String disclaimer;
        private String chartName;
        private Map<String, BpiDTO> bpi;

        public Builder() {
        }

        public Builder setTime(Map<String, String> time) {
            this.time = time;
            return this;
        }

        public Builder setDisclaimer(String disclaimer) {
            this.disclaimer = disclaimer;
            return this;
        }

        public Builder setChartName(String chartName) {
            this.chartName = chartName;
            return this;
        }

        public Builder setBpi(Map<String, BpiDTO> bpi) {
            this.bpi = bpi;
            return this;
        }

        public CurrentPriceDTO build() {
            CurrentPriceDTO currentPriceDTO = new CurrentPriceDTO();
            currentPriceDTO.time = this.time;
            currentPriceDTO.disclaimer = this.disclaimer;
            currentPriceDTO.chartName = this.chartName;
            currentPriceDTO.bpi = this.bpi;
            return currentPriceDTO;
        }
    }
}
