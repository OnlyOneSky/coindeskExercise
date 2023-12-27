package com.chc.coindesk.service;

import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.dto.CurrentPriceDTO;
import com.chc.coindesk.model.*;
import com.chc.coindesk.repository.BpiRepository;
import com.chc.coindesk.repository.CurrencyRepository;
import com.chc.coindesk.repository.FetchInfoRepository;
import com.chc.coindesk.repository.TranslationRepository;
import com.chc.coindesk.util.DateUtil;
import com.chc.coindesk.util.PolyglotField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BpiService {
    private static final Logger logger = LoggerFactory.getLogger(BpiService.class);
    @Value("${coindesk.currentprice.url}")
    private String coinDeskUrl;
    private RestTemplate restTemplate;
    private CurrencyRepository currencyRepository;

    private FetchInfoRepository fetchInfoRepository;

    private TranslationRepository translationRepository;

    private BpiRepository bpiRepository;

    private UpdateBPIFacade updateBPIFacade;

    public BpiService(RestTemplate restTemplate, CurrencyRepository currencyRepository,
                      BpiRepository bpiRepository, FetchInfoRepository fetchInfoRepository,
                      TranslationRepository translationRepository, UpdateBPIFacade updateBPIFacade) {
        this.restTemplate = restTemplate;
        this.currencyRepository = currencyRepository;
        this.bpiRepository = bpiRepository;
        this.fetchInfoRepository = fetchInfoRepository;
        this.translationRepository = translationRepository;
        this.updateBPIFacade = updateBPIFacade;
    }

    @Transactional
    @Scheduled(fixedDelay = 10000)
    public boolean updateCurrentPrices() {
        String responseJson;
        try {
            responseJson = restTemplate.getForObject(coinDeskUrl, String.class);
        } catch (Exception e) {
            return false;
        }
        logger.info(String.format("update-price Response: %s", responseJson));

        ObjectMapper objectMapper = new ObjectMapper();
        CurrentPriceDTO bitcoinData;
        try {
            bitcoinData = objectMapper.readValue(responseJson, CurrentPriceDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        updateBPIFacade.setBitcoinData(bitcoinData);
        updateBPIFacade.doUpdate();
        return true;
    }


    @Transactional
    public boolean addOrUpdateBpi(BpiDTO bpiDTO) {
        try {
            Bpi bpi = prepareBpiFromDTO(bpiDTO);
            int newCurrencyId = getCurrencyId(bpiDTO);
            handleTranslation(newCurrencyId, bpiDTO.getDescription());
            bpiRepository.save(bpi);
            return true;
        } catch (DataAccessException e) {
            logger.error("Error during adding or updating Bpi: " + e.getMessage());
            return false;
        }
    }

    private Bpi prepareBpiFromDTO(BpiDTO bpiDTO) {
        Bpi bpi = new Bpi();
        bpi.setCurrencyCode(bpiDTO.getCode());
        bpi.setSymbol(bpiDTO.getSymbol());
        bpi.setRate(bpiDTO.getRate());
        bpi.setDescription(PolyglotField.CURRENCY_DESCRIPTION.getId());
        bpi.setRateFloat(bpiDTO.getRate_float());
        bpi.setCreated(LocalDateTime.now());
        bpi.setUpdated(LocalDateTime.now());
        return bpi;
    }

    private int getCurrencyId(BpiDTO bpiDTO) {
        Currency currency = currencyRepository.findByCurrencyCode(bpiDTO.getCode());
        if (currency == null) {
            int highestId = currencyRepository.findTopByOrderByIdDesc().getId();
            currency = new Currency();
            currency.setId(highestId + 1);
            currency.setCurrencyCode(bpiDTO.getCode());
            currencyRepository.save(currency);
            return highestId + 1;
        } else {
            return currency.getId();
        }
    }

    private void handleTranslation(int newCurrencyId, String description) {
        TranslationKey translationKey = new TranslationKey();
        translationKey.setLanguageId(1); //English
        translationKey.setTextColumnId(1); //currency_description
        translationKey.setTranslatingTableUid(newCurrencyId);
        Optional<Translation> translationOpt = translationRepository.findById(translationKey);
        Translation translation = translationOpt.orElse(new Translation());
        translation.setTranslationKey(translationKey);
        translation.setTranslation(description);
        translationRepository.save(translation);
    }

    public void delete(String currencyCode) {
        Bpi bpi = new Bpi();
        bpi.setCurrencyCode(currencyCode);

        bpiRepository.delete(bpi);
    }

    public BpiDTO findByCodeAndLanguage(String currencyCode, int languageId) {
        return bpiRepository.findByCodeAndLanguageId(currencyCode, languageId);
    }

    public CurrentPriceDTO lookupAllBpi(int languageId) {
        FetchInfo latestFetchInfo = fetchInfoRepository.findFirstByOrderByIdDesc();
        Map<String, String> timeMap = new HashMap<>();
        timeMap.put("updated", DateUtil.updateukDateToLocalDateTime(latestFetchInfo.getUpdate(), "MMM dd, yyyy HH:mm:ss z"));
        timeMap.put("updatedISO", DateUtil.updateukDateToLocalDateTime(latestFetchInfo.getUpdatedISO(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
        timeMap.put("updateduk", DateUtil.updateukDateToLocalDateTime(latestFetchInfo.getUpdateduk(), "MMM dd, yyyy 'at' HH:mm z"));

        List<BpiDTO> bpiList = bpiRepository.findAllInLanguageId(languageId);
        Map<String, BpiDTO> bpiMap = new HashMap<>();

        for (BpiDTO bpi : bpiList) {
            bpiMap.put(bpi.getCode(), bpi);
        }

        return new CurrentPriceDTO.Builder()
                .setTime(timeMap)
                .setDisclaimer(latestFetchInfo.getDisclaimer())
                .setChartName(latestFetchInfo.getChartName())
                .setBpi(bpiMap)
                .build();
    }
}
