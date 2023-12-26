package com.chc.coindesk.controller;

import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.service.BpiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        logger.info(String.format("update-price Request body %s", requestBody));
        bpiService.updateCurrentPrices();
    }

    @PostMapping("/add-new")
    public void addNew(@RequestBody BpiDTO bpiDTO) {
        logger.info(String.format("add-new Request body %s", bpiDTO.toString()));
        bpiService.addOrUpdateBpi(bpiDTO);
    }

    @PostMapping("/update")
    public void update(@RequestBody BpiDTO bpiDTO) {
        logger.info(String.format("update Request body %s", bpiDTO.toString()));
        bpiService.addOrUpdateBpi(bpiDTO);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody Map<String, String> request) {
        logger.info(String.format("delete Request body %s", request.toString()));
        bpiService.delete(request.get("code"));
    }

    @GetMapping("/lookup-individual")
    public BpiDTO lookupByCodeAndLanguageId(@RequestBody Map<String, Object> request) {
        logger.info(String.format("lookup Request body %s", request.toString()));
        return bpiService.findByCodeAndLanguage((String)request.get("code"), (int)request.get("language_id"));
    }

}
