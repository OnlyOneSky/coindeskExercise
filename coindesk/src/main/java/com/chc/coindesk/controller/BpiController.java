package com.chc.coindesk.controller;

import com.chc.coindesk.dto.BpiDTO;
import com.chc.coindesk.dto.DeleteDTO;
import com.chc.coindesk.service.BpiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Tag(name = "BPI API")
@RequestMapping("/api/v1/coindesk")
public class BpiController {
    private static final Logger logger = LoggerFactory.getLogger(BpiController.class);
    private BpiService bpiService;

    public BpiController(BpiService bpiService) {
        this.bpiService = bpiService;
    }

    @Operation(summary = "Invoke API on coindesk.", description = "Retrieve data from Bitcoin and update local DB.")
    @GetMapping("/invoke-coindesk")
    public ResponseEntity<?> updateCurrentPrice() {
        logger.info(String.format("update-price doesn't take any input."));
        if (bpiService.updateCurrentPrices()) {
            return new ResponseEntity<>("Retrieve and update data successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Retrieve and update data failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Add new BPI", description = "Add a new bpi record to local DB.")
    @PostMapping("/add-new")
    public ResponseEntity<?> addNew(@RequestBody BpiDTO bpiDTO) {
        logger.info(String.format("add-new Request body %s", bpiDTO.toString()));
        try {
            boolean isAdded = bpiService.addOrUpdateBpi(bpiDTO);
            if (isAdded) {
                return new ResponseEntity<>("Data added successfully.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Data could not be added.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while adding new BPI: ", e);
            return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update BPI", description = "Update a bpi record on local DB.")
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody BpiDTO bpiDTO) {
        logger.info(String.format("update Request body %s", bpiDTO.toString()));
        try {
            boolean isAdded = bpiService.addOrUpdateBpi(bpiDTO);
            if (isAdded) {
                return new ResponseEntity<>("Data updated successfully.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Data could not be updated.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while updating new BPI: ", e);
            return new ResponseEntity<>("An error occurred while processing your update request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a BPI", description = "Delete a chosen BPI.")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody DeleteDTO request) {
        logger.info(String.format("delete Request body %s", request.toString()));
        try {
            bpiService.delete(request.getCode());
            return new ResponseEntity<>("Data deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception occurred while deleting BPI: ", e);
            return new ResponseEntity<>("An error occurred while processing your delete request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Lookup a BPI",
            description = "Return the BPI information in the chosen language. 1:English 2.Chinese",
            responses = @ApiResponse(
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BpiDTO.class)))
                    }))
    @GetMapping("/lookup-individual/{currencyCode}/{languageId}")//Get request on OpenApi 3.0 can't take request body
    public ResponseEntity<?> lookupByCodeAndLanguageId(@PathVariable @Parameter(name = "currencyCode", description = "Currency Code", example = "USD") String currencyCode,
                                                       @PathVariable @Parameter(name = "languageId", description = "Language id (1:English 2:Chinese)", example = "2") int languageId) {
        logger.info(String.format("lookup individual path variables currencyCode = %s languageId = %d", currencyCode, languageId));
        try {
            return new ResponseEntity<>(bpiService.findByCodeAndLanguage(currencyCode, languageId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception occurred while looking up individual BPI: ", e);
            return new ResponseEntity<>("An error occurred while processing your lookup request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Lookup all BPIs", description = "Return all BPIs information in the chosen language. 1:English 2.Chinese")
    @GetMapping("/lookup-all/{languageId}")
    public ResponseEntity<?> lookupAllInLanguageId(@PathVariable @Parameter(name = "languageId", description = "Language id (1:English 2:Chinese)", example = "1") int languageId) {
        logger.info(String.format("lookup-all Request path variable %s", languageId));
        try {
            return new ResponseEntity<>(bpiService.lookupAllBpi(languageId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception occurred while looking up all BPIs: ", e);
            return new ResponseEntity<>("An error occurred while processing your lookup request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
