package com.afvergani.backendtest.service;

import org.springframework.http.ResponseEntity;

public interface IProductService {

    ResponseEntity<?> getSimilarProducts(String requestId,String productId);

}
