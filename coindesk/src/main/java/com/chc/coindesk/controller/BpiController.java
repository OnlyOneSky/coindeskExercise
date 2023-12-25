package com.chc.coindesk.controller;

import com.chc.coindesk.service.BpiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coindesk")
public class BpiController {
    private static final Logger logger = LoggerFactory.getLogger(BpiController.class);
    private BpiService bpiService;

    public BpiController(BpiService bpiService) {
        this.bpiService = bpiService;
    }

    @GetMapping("/updateprices")
    public void updateCurrentPrice(@RequestBody String requestBody) {
        logger.info(String.format("Request body %s", requestBody));
        bpiService.updateCurrentPrices();
    }
}
