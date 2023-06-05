package com.afvergani.backendtest.controller;

import com.afvergani.backendtest.service.IProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;


@RestController
public class ServiceController {

    IProductService productService;
    private static final Logger logger = LogManager.getLogger(ServiceController.class);
    private Integer counter = 0;
    public ServiceController(IProductService productService) {
        this.productService = productService;
    }

    /**
     * Get similar products
     * @param productId
     * @return ResponseEntity<>(List<Product>, HttpStatus)
     */
    @GetMapping("/product/{productId}/similar")
    public ResponseEntity<?> getSimilarProducts(@PathVariable String productId) {

        //Generate a random UUID for each request
        String requestId = UUID.randomUUID().toString();
        logger.info(requestId + " | New Request: " + requestId);

        try {
            ResponseEntity<?> response = productService.getSimilarProducts(requestId, productId);
            logger.info(requestId + " | Controller: " + Objects.requireNonNull(response.toString()));
            return response;
        } catch (Exception e) {
            logger.error(requestId + " | Controller - Error: " + e.getMessage());
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }

    }
}

