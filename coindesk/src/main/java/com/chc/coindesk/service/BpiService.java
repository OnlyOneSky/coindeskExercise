package com.chc.coindesk.service;

import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.dto.CurrentPriceDTO;
import com.chc.coindesk.model.*;
import com.chc.coindesk.repository.BpiRepository;
import com.chc.coindesk.repository.CurrencyRepository;
import com.chc.coindesk.repository.FetchInfoRepository;
import com.chc.coindesk.repository.TranslationRepository;
import com.chc.coindesk.util.PolyglotField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final String coinDeskUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private RestTemplate restTemplate;
    private CurrencyRepository currencyRepository;

    private FetchInfoRepository fetchInfoRepository;

    private TranslationRepository translationRepository;

    BpiRepository bpiRepository;

    public BpiService(RestTemplate restTemplate, CurrencyRepository currencyRepository,
                      BpiRepository bpiRepository, FetchInfoRepository fetchInfoRepository,
                      TranslationRepository translationRepository) {
        this.restTemplate = restTemplate;
        this.currencyRepository = currencyRepository;
        this.bpiRepository = bpiRepository;
        this.fetchInfoRepository = fetchInfoRepository;
        this.translationRepository = translationRepository;
    }

    @Transactional
    public void updateCurrentPrices() {
        String responseJson = restTemplate.getForObject(coinDeskUrl, String.class);
        logger.info(String.format("update-price Response: %s", responseJson));

        ObjectMapper objectMapper = new ObjectMapper();
        CurrentPriceDTO bitcoinData;
        try {
            bitcoinData = objectMapper.readValue(responseJson, CurrentPriceDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        FetchInfo fetchInfo = saveFetchInfo(bitcoinData);
        fetchInfoRepository.save(fetchInfo);

        List<Bpi> bpiList = transferToBPIs(bitcoinData);
        bpiRepository.saveAll(bpiList);
    }

    private static FetchInfo saveFetchInfo(CurrentPriceDTO bitcoinData) {
        FetchInfo fetchInfo = new FetchInfo();

        bitcoinData.getTime().values().forEach(str -> System.out.println(str));
        for(Map.Entry<String, String> entry: bitcoinData.getTime().entrySet()) {
            switch(entry.getKey()) {
                case "updated":
                    fetchInfo.setUpdate(entry.getValue());
                    break;
                case "updatedISO":
                    fetchInfo.setUpdatedISO(entry.getValue());
                    break;
                default:
                    fetchInfo.setUpdateduk(entry.getValue());
            }
        }

        fetchInfo.setDisclaimer(bitcoinData.getDisclaimer());
        fetchInfo.setChartName(bitcoinData.getChartName());
        return fetchInfo;
    }
    //query one
    //query all

    @Transactional
    public boolean addOrUpdateBpi(BpiDTO bpiDTO) {
        Bpi bpi = new Bpi();
        bpi.setCurrencyCode(bpiDTO.getCode());
        bpi.setSymbol(bpiDTO.getSymbol());
        bpi.setRate(bpiDTO.getRate());
        bpi.setDescription(PolyglotField.CURRENCY_DESCRIPTION.getId());
        bpi.setRateFloat(bpiDTO.getRate_float());
        bpi.setCreated(LocalDateTime.now());
        bpi.setUpdated(LocalDateTime.now());
        //TO-DO: ADD NEW CURRENCY
        Currency currency = currencyRepository.findByCurrencyCode(bpiDTO.getCode());
        int newCurrencyId;
        if (currency == null) {
            int highestId = currencyRepository.findTopByOrderByIdDesc().getId();
            newCurrencyId = highestId + 1;
            currency = new Currency();
            currency.setId(newCurrencyId);
            currency.setCurrencyCode(bpiDTO.getCode());
            currencyRepository.save(currency);
        } else {
            newCurrencyId = currency.getId();
        }

        TranslationKey translationKey = new TranslationKey();
        translationKey.setLanguageId(1); //English
        translationKey.setTextColumnId(1); //currency_description
        translationKey.setTranslatingTableUid(newCurrencyId);

        Optional translationOpt = translationRepository.findById(translationKey);
        Translation translation;
        if (translationOpt.isPresent()) {
            translation = (Translation) translationOpt.get();
            translation.setTranslation(bpiDTO.getDescription());
        } else {
            translation = new Translation();
            translation.setTranslationKey(translationKey);
            translation.setTranslation(bpiDTO.getDescription());
        }

        translationRepository.save(translation);

        bpiRepository.save(bpi);

        return true;
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
        timeMap.put("updated", latestFetchInfo.getUpdate());
        timeMap.put("updatedISO", latestFetchInfo.getUpdatedISO());
        timeMap.put("updateduk", latestFetchInfo.getUpdateduk());

        List<Bpi> bpiList = bpiRepository.findAll();


        return null;

    }


    private List<Bpi> transferToBPIs(CurrentPriceDTO bitcoinData) {

        FetchInfo fetchInfo = new FetchInfo();
        return bitcoinData.getBpi().values().stream()
                .map(currency -> {
                    Bpi bpi = new Bpi();
                    //Skip updating if the currency not exist
                    Currency currentCurrency = this.currencyRepository.findIdByCurrencyCode(currency.getCode());
                    if (currentCurrency == null) {
                        return null;
                    }

                    bpi.setCurrencyCode(currency.getCode());
                    bpi.setSymbol(currency.getSymbol());
                    bpi.setRate(currency.getRate());
                    bpi.setDescription(PolyglotField.CURRENCY_DESCRIPTION.getId());
                    bpi.setRateFloat(currency.getRate_float());
                    bpi.setCreated(LocalDateTime.now());
                    bpi.setUpdated(LocalDateTime.now());

                    return bpi;
                }).collect(Collectors.toList());
    }
}
