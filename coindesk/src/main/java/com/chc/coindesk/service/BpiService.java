package com.chc.coindesk.service;

import com.chc.coindesk.dto.CurrentPrice;
import com.chc.coindesk.model.Bpi;
import com.chc.coindesk.model.Currency;
import com.chc.coindesk.repository.BpiRepository;
import com.chc.coindesk.repository.CurrencyRepository;
import com.chc.coindesk.util.PolyglotField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BpiService {
    private static final Logger logger = LoggerFactory.getLogger(BpiService.class);
    private final String coinDeskUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private RestTemplate restTemplate;
    private CurrencyRepository currencyRepository;
    BpiRepository bpiRepository;

    public BpiService(RestTemplate restTemplate, CurrencyRepository currencyRepository,
                      BpiRepository bpiRepository) {
        this.restTemplate = restTemplate;
        this.currencyRepository = currencyRepository;
        this.bpiRepository = bpiRepository;
    }

    @Transactional
    public void updateCurrentPrices() {
        String responseJson = restTemplate.getForObject(coinDeskUrl, String.class);
        logger.info(String.format("Response from coindesk: %s", responseJson));

        List<Bpi> bpiList = transferToBPIs(responseJson);

        bpiRepository.saveAll(bpiList);
    }

    private List<Bpi> transferToBPIs(String responseJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CurrentPrice bitcoinData = objectMapper.readValue(responseJson, CurrentPrice.class);

            return bitcoinData.getBpi().values().stream()
                    .map(currency -> {
                        Bpi bpi = new Bpi();

                        Currency currentCurrency = this.currencyRepository.findIdByCurrencyCode(currency.getCode());
                        if(currentCurrency == null) {
                            return null;
                        }

                        bpi.setCurrency_code(currentCurrency.getId());
                        bpi.setSymbol(currency.getSymbol());
                        bpi.setRate(currency.getRate());
                        bpi.setDescription(PolyglotField.CURRENCY_DESCRIPTION.getId());
                        bpi.setRate_float(currency.getRate_float());
                        bpi.setCreated(LocalDateTime.now());
                        bpi.setUpdated(LocalDateTime.now());

                        return bpi;
                    }).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
