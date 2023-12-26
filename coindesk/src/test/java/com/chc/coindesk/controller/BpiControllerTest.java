package com.chc.coindesk.controller;

import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.service.BpiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void testUpdateCurrentPrice() throws Exception {
        mockMvc.perform(get("/api/v1/coindesk/update-prices")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                .andExpect(status().isOk());

        verify(bpiService, times(1)).updateCurrentPrices();
    }

    @Test
    public void deleteSuccessfully() throws Exception {
        String code = "USD";

        mockMvc.perform(delete("/api/v1/coindesk/delete")
                        .content("{\"code\":\"" + code + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bpiService, times(1)).delete(code);
    }
}