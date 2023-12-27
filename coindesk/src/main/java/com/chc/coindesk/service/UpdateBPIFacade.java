package com.chc.coindesk.service;

import com.chc.coindesk.dto.CurrentPriceDTO;
import com.chc.coindesk.model.Bpi;
import com.chc.coindesk.model.Currency;
import com.chc.coindesk.model.FetchInfo;
import com.chc.coindesk.repository.BpiRepository;
import com.chc.coindesk.repository.CurrencyRepository;
import com.chc.coindesk.repository.FetchInfoRepository;
import com.chc.coindesk.util.PolyglotField;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Facade class for updating BPI data.
 */
@Service
public class UpdateBPIFacade {
    private FetchInfoRepository fetchInfoRepository;
    private BpiRepository bpiRepository;

    private CurrencyRepository currencyRepository;

    @Setter
    private CurrentPriceDTO bitcoinData;

    public UpdateBPIFacade(FetchInfoRepository fetchInfoRepository, BpiRepository bpiRepository,
                           CurrencyRepository currencyRepository) {
        this.fetchInfoRepository = fetchInfoRepository;
        this.bpiRepository = bpiRepository;
        this.currencyRepository = currencyRepository;
    }

    public void doUpdate() throws IllegalArgumentException{
        if(bitcoinData == null) {
            throw new IllegalArgumentException("Required data is not available!!");
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

    private List<Bpi> transferToBPIs(CurrentPriceDTO bitcoinData) {

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
