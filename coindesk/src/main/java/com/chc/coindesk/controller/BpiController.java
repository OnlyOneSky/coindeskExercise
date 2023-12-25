package com.chc.coindesk.controller;

import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.model.Bpi;
import com.chc.coindesk.service.BpiService;
import com.chc.coindesk.util.PolyglotField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/v1/coindesk")
public class BpiController {
    private static final Logger logger = LoggerFactory.getLogger(BpiController.class);
    private BpiService bpiService;

    public BpiController(BpiService bpiService) {
        this.bpiService = bpiService;
    }

    @GetMapping("/update-prices")
    public void updateCurrentPrice(@RequestBody String requestBody) {
        logger.info(String.format("Request body %s", requestBody));
        bpiService.updateCurrentPrices();
    }

    @PostMapping("/add-new")
    public void addNew(@RequestBody BpiDTO bpiDTO) {
        bpiService.addOrUpdate(bpiDTO);
    }

    @PostMapping("/update")
    public void update(@RequestBody BpiDTO bpiDTO) {
        bpiService.addOrUpdate(bpiDTO);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody Map<String, String> request) {
        bpiService.delete(request.get("code"));
    }



    //Add new
    //Update
    //Delete
    //query one
    //query all
}
