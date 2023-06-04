package com.afvergani.backendtest.service;

import com.afvergani.backendtest.core.service.ICoreService;
import com.afvergani.backendtest.model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements IProductService {

    ICoreService coreService;
    private static final Logger logger = LogManager.getLogger(ProductService.class);


    public ProductService(ICoreService coreService) {
        this.coreService = coreService;
    }

        /**
         * Get list of similar products
         * @param requestId
         * @param productId
         * @return List(String)
         */
        @Override
        public ResponseEntity<?> getSimilarProducts(String requestId, String productId) {

        try {
                List<String> similarProductIds = coreService.getSimilarProductsIds(requestId, productId);
                List<Product> similarProducts = new ArrayList<>();
                if(similarProductIds != null) {
                    for (String id : similarProductIds) {
                        logger.info(requestId + " | ProductService-id: " + id);
                        Product product = coreService.getProductDetailService(requestId, id);
                        logger.debug(requestId + " | ProductService - Product detail: " + product);
                        similarProducts.add(product);
                    }
                    return new ResponseEntity<>(similarProducts, HttpStatus.OK);
                }

            } catch (Exception e) {
                logger.error(requestId + " | ProductService - Error: " + e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
}
