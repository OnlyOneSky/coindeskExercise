package com.chc.coindesk.controller;

import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.dto.DeleteDTO;
import com.chc.coindesk.dto.LookUpDTO;
import com.chc.coindesk.service.BpiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class BpiControllerTest {

    @MockBean
    BpiService bpiService;

    @Mock
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new BpiController(bpiService)).build();
    }


    @Test
    public void testUpdateCurrentPriceSuccess() throws Exception {
        // isSuccess is the result of the service operation
        boolean isSuccess = true;

        // Define the behavior of bpiService when the updateCurrentPrices() method is called
        when(bpiService.updateCurrentPrices()).thenReturn(isSuccess);

        // Perform the request and check the result
        mockMvc.perform(get("/api/v1/coindesk/invoke-coindesk")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Retrieve and update data successfully."));
    }

    @Test
    public void testUpdateCurrentPriceFailure() throws Exception {
        // isSuccess is the result of the service operation
        boolean isSuccess = false;

        // Define the behavior of bpiService when the updateCurrentPrices() method is called.
        when(bpiService.updateCurrentPrices()).thenReturn(isSuccess);

        // Perform the request and check the result
        mockMvc.perform(get("/api/v1/coindesk/invoke")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(""));
    }

    @Test
    public void addNewBpiTest() throws Exception {
        BpiDTO bpiDTO = new BpiDTO();
        bpiDTO.setCode("ABC");
        bpiDTO.setSymbol("&#38;");
        bpiDTO.setRate("43,264.2424");
        bpiDTO.setDescription("ABC Dollar");
        bpiDTO.setRate_float(43264.2424);

        when(bpiService.addOrUpdateBpi(any())).thenReturn(true);

        mockMvc.perform(post("/api/v1/coindesk/add-new")
                        .content("{\"code\": \"ABC\", \"symbol\": \"&#38;\", \"rate\": \"43,264.2424\", \"description\": \"ABC Dollar\", \"rate_float\": 43264.2424}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void addNewBpiFailedTest() throws Exception {
        BpiDTO bpiDTO = new BpiDTO();
        bpiDTO.setCode("ABC");
        bpiDTO.setSymbol("&#38;");
        bpiDTO.setRate("43,264.2424");
        bpiDTO.setDescription("ABC Dollar");
        bpiDTO.setRate_float(43264.2424);

        when(bpiService.addOrUpdateBpi(any())).thenReturn(false);

        mockMvc.perform(post("/api/v1/coindesk/add-new")
                        .content("{\"code\": \"ABC\", \"symbol\": \"&#38;\", \"rate\": \"43,264.2424\", \"description\": \"ABC Dollar\", \"rate_float\": 43264.2424}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void updateTest() throws Exception {
        BpiDTO bpiDTO = new BpiDTO();
        bpiDTO.setCode("ABC");
        bpiDTO.setSymbol("&#38;");
        bpiDTO.setRate("43,264.2424");
        bpiDTO.setDescription("ABC Dollar");
        bpiDTO.setRate_float(43264.2424);

        when(bpiService.addOrUpdateBpi(any())).thenReturn(true);

        mockMvc.perform(post("/api/v1/coindesk/update")
                        .content("{\"code\": \"ABC\", \"symbol\": \"&#38;\", \"rate\": \"43,264.2424\", \"description\": \"ABC Dollar\", \"rate_float\": 43264.2424}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateFailedTest() throws Exception {
        BpiDTO bpiDTO = new BpiDTO();
        bpiDTO.setCode("ABC");
        bpiDTO.setSymbol("&#38;");
        bpiDTO.setRate("43,264.2424");
        bpiDTO.setDescription("ABC Dollar");
        bpiDTO.setRate_float(43264.2424);

        when(bpiService.addOrUpdateBpi(any())).thenReturn(false);

        mockMvc.perform(post("/api/v1/coindesk/update")
                        .content("{\"code\": \"ABC\", \"symbol\": \"&#38;\", \"rate\": \"43,264.2424\", \"description\": \"ABC Dollar\", \"rate_float\": 43264.2424}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testDele() throws Exception {
        DeleteDTO request = new DeleteDTO();
        request.setCode("ABC");

        doNothing().when(bpiService).delete("ABC");

        mockMvc.perform(delete("/api/v1/coindesk/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"ABC\"}"))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void testDeleteFailed() throws Exception {
        DeleteDTO request = new DeleteDTO();
        request.setCode("ABC");

        doThrow(new RuntimeException()).when(bpiService).delete("ABC");

        mockMvc.perform(delete("/api/v1/coindesk/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"ABC\"}"))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    void testLookupByCodeAndLanguageIdSuccess() throws Exception {
        LookUpDTO request = new LookUpDTO();
        request.setCode("USA");
        request.setLanguage_id(1);

        ObjectMapper objectMapper = new ObjectMapper();

        when(bpiService.findByCodeAndLanguage(eq(request.getCode()), eq(request.getLanguage_id()))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/coindesk/lookup-individual")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testLookupByCodeAndLanguageIdBadRequest() throws Exception {
        LookUpDTO request = new LookUpDTO();
        request.setLanguage_id(1);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/coindesk/lookup-individual")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testLookupByCodeAndLanguageIdInternalServerError() throws Exception {
        LookUpDTO request = new LookUpDTO();
        request.setCode("USA");
        request.setLanguage_id(1);

        ObjectMapper objectMapper = new ObjectMapper();

        when(bpiService.findByCodeAndLanguage(eq(request.getCode()), eq(request.getLanguage_id()))).thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/coindesk/lookup-individual")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void testLookupAllInLanguageIdException() throws Exception {
        int languageId = 3;
        String url = "/api/v1/coindesk/lookup-all/"+languageId;

        when(bpiService.lookupAllBpi(languageId)).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

}