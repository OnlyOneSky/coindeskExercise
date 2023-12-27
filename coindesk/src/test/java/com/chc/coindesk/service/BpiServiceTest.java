package com.chc.coindesk.service;

import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.dto.CurrentPriceDTO;
import com.chc.coindesk.model.Bpi;
import com.chc.coindesk.model.Currency;
import com.chc.coindesk.model.FetchInfo;
import com.chc.coindesk.repository.BpiRepository;
import com.chc.coindesk.repository.CurrencyRepository;
import com.chc.coindesk.repository.FetchInfoRepository;
import com.chc.coindesk.repository.TranslationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class BpiServiceTest {

    CurrencyRepository currencyRepository = Mockito.mock(CurrencyRepository.class);
    BpiRepository bpiRepository = Mockito.mock(BpiRepository.class);
    TranslationRepository translationRepository = Mockito.mock(TranslationRepository.class);

    FetchInfoRepository fetchInfoRepository = Mockito.mock(FetchInfoRepository.class);
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    BpiService bpiService = new BpiService(restTemplate, currencyRepository, bpiRepository, fetchInfoRepository, translationRepository);

/*    @Test
    public void testUpdateCurrentPrices() throws Exception {
        CurrentPriceDTO currentPriceDTO = new CurrentPriceDTO.Builder()
                .setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD).")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String validResponse = objectMapper.writeValueAsString(currentPriceDTO);

        when(restTemplate.getForObject(anyString(), anyString().getClass()))
                .thenReturn(validResponse);

        boolean result = bpiService.updateCurrentPrices();
        assertThat(result).isTrue();

        when(restTemplate.getForObject(anyString(), anyString().getClass())).thenThrow(RuntimeException.class);
        result = bpiService.updateCurrentPrices();
        assertThat(result).isFalse();
    }*/

    @Test
    public void testAddOrUpdateBpi_successCase() {
        BpiDTO bpiDTO = new BpiDTO(
                "USD",
                "$",
                "8000.2412",
                "American Dollar",
                8000.2412);

        com.chc.coindesk.model.Currency currency = new Currency();
        currency.setId(1);
        currency.setCurrencyCode("USD");
        // Simulate behaviour defined in the BpiService code
        Mockito.when(currencyRepository.findByCurrencyCode("USD")).thenReturn(currency);

        // Execute method
        boolean result = bpiService.addOrUpdateBpi(bpiDTO);

        // Assert result
        assertTrue(result);
    }

    @Test
    public void deleteBpi() {
        Bpi expectedBpi = new Bpi();
        String testCurrencyCode = "USD";
        expectedBpi.setCurrencyCode(testCurrencyCode);

        bpiService.delete(testCurrencyCode);

        // Verifying that the delete method in Bpi Repository
        // is invoked once with the right Bpi object.
        verify(bpiRepository, times(1)).delete(expectedBpi);
    }

    @Test
    public void testLookupAllBpi() {

        FetchInfo fetchInfo = new FetchInfo();
        fetchInfo.setUpdate("Nov 14, 2021 00:51:00 UTC");
        fetchInfo.setUpdatedISO("2021-11-14T00:51:00Z");
        fetchInfo.setUpdateduk("Nov 14, 2021 at 00:51 GMT");
        fetchInfo.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD)");
        fetchInfo.setChartName("Bitcoin");

        Mockito.when(this.fetchInfoRepository.findFirstByOrderByIdDesc())
                .thenReturn(fetchInfo);

        List<BpiDTO> bpiDTOList = new ArrayList<>();
        BpiDTO bpiModel = new BpiDTO();
        bpiModel.setSymbol("$");
        bpiModel.setRate("1,000.00");
        bpiModel.setRate_float(1000.00d);
        bpiModel.setCode("USD");
        bpiDTOList.add(bpiModel);
        Mockito
                .when(this.bpiRepository.findAllInLanguageId(1))
                .thenReturn(bpiDTOList);

        CurrentPriceDTO actual = this.bpiService.lookupAllBpi(1);
//
//        Assertions.assertEquals(fetchInfo.getUpdateduk(), actual.getTime().get("updateduk"));
//        Assertions.assertEquals(fetchInfo.getUpdate(), actual.getTime().get("updated"));
//        Assertions.assertEquals(fetchInfo.getUpdatedISO(), actual.getTime().get("updatedISO"));

        Assertions.assertNotNull(actual.getBpi().get("USD"));
        Assertions.assertEquals(bpiModel.getRate(), actual.getBpi().get("USD").getRate());
        Assertions.assertEquals(bpiModel.getRate_float(), actual.getBpi().get("USD").getRate_float());
        Assertions.assertEquals(bpiModel.getSymbol(), actual.getBpi().get("USD").getSymbol());
    }
}