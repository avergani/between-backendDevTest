package com.afvergani.backendtest.core.service;

import com.afvergani.backendtest.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Setter
public class CoreService implements ICoreService {

    @Value("${get.similar.products.id.url}")
    private String similarProductIdUrl;

    @Value("${get.product.detail.url}")
    private String productDetailUrl;
    HttpClient httpClient = HttpClient.newHttpClient();
    private static final Logger logger = LogManager.getLogger(CoreService.class);

    /**
     * Get similar products ids
     * @param requestId
     * @param productId
     * @return List(String)
     */
    public List<String> getSimilarProductsIds(String requestId, String productId) {

        logger.info(requestId + " | CoreService-similar id: " + productId);

        List<String> similarProductsIds;
        //URI similarProductUri = URI.create(similarProductIdUrl + "/" + productId + "/similarids");


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(similarProductIdUrl + "/" + productId + "/similarids"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                String responseString = response.body().replace("[", "").replace("]", "");
                similarProductsIds = List.of(responseString.split(","));
                logger.debug(requestId + " | CoreService-similar products: " + similarProductsIds);
                return similarProductsIds;
            }else{
                return null;
            }

        } catch (IOException | InterruptedException e) {
            logger.error(requestId + " | CoreService-: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get product details
     * @param requestId
     * @param productId
     * @return Product
     */
    public Product getProductDetailService(String requestId, String productId) {

        logger.info(requestId + " | CoreService-product id: " + productId);

        URI productDetailUri = URI.create(productDetailUrl + "/" + productId);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(productDetailUri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                Product product = objectMapper.readValue(response.body(), Product.class);
                logger.debug(requestId + " | CoreService-product: " + productId + " details: " + product);
                return product;
            }else{
                return null;
            }

        } catch (IOException | InterruptedException e) {
            logger.error(requestId + " | CoreService-: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}

