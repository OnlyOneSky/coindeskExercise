package com.chc.coindesk.service;

import com.chc.coindesk.dto.BpiConvertedDTO;
import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.dto.CurrentPrice;
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
import java.util.List;
import java.util.Map;
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
        logger.info(String.format("Response from coindesk: %s", responseJson));

        ObjectMapper objectMapper = new ObjectMapper();
        CurrentPrice bitcoinData;
        try {
            bitcoinData = objectMapper.readValue(responseJson, CurrentPrice.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        FetchInfo fetchInfo = saveFetchInfo(bitcoinData);
        fetchInfoRepository.save(fetchInfo);

        List<Bpi> bpiList = transferToBPIs(bitcoinData);
        bpiRepository.saveAll(bpiList);
    }

    private static FetchInfo saveFetchInfo(CurrentPrice bitcoinData) {
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

    //split save and Update
    //Delete currency -also need to delete translation
    //query one
    //query all

    @Transactional
    public boolean addOrUpdate(BpiDTO bpiDTO) {
        Bpi bpi = new Bpi();
        bpi.setCurrencyCode(bpiDTO.getCode());
        bpi.setSymbol(bpiDTO.getSymbol());
        bpi.setRate(bpiDTO.getRate());
        bpi.setDescription(PolyglotField.CURRENCY_DESCRIPTION.getId());
        bpi.setRateFloat(bpiDTO.getRate_float());
        bpi.setCreated(LocalDateTime.now());
        bpi.setUpdated(LocalDateTime.now());
        //TO-DO: ADD NEW CURRENCY
        int highestId = currencyRepository.findTopByOOrderByIdDesc().getId();
        int newCurrencyId = highestId + 1;
        Currency currency = new Currency();
        currency.setId(newCurrencyId);
        currency.setCurrencyCode(bpiDTO.getCode());
        currencyRepository.save(currency);

        //TO-DO: ADD NEW DEFAULT LANGUAGE TRANSLATION - EN
        TranslationKey translationKey = new TranslationKey();
        translationKey.setLanguageId(1); //English
        translationKey.setTextColumnId(1); //currency_description
        translationKey.setTranslatingTableUid(newCurrencyId);

        Translation translation = new Translation();
        translation.setTranslationKey(translationKey);
        translation.setTranslation("New currency " + bpiDTO.getCode());

        translationRepository.save(translation);

        bpiRepository.save(bpi);

        return true;
    }

    public void delete(String currencyCode) {
        Bpi bpi = new Bpi();
        bpi.setCurrencyCode(currencyCode);

        bpiRepository.delete(bpi);
    }

    public BpiConvertedDTO lookupByCode(String code) {
        Bpi bpi = bpiRepository.findByCurrencyCode(code);
        return new BpiConvertedDTO.Builder()
                .code(bpi.getCurrencyCode())
                .symbol(bpi.getSymbol())
                .rate(bpi.getRate())
                .build();

    }


    private List<Bpi> transferToBPIs(CurrentPrice bitcoinData) {

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
