package com.afvergani.backendtest.core.service;

import com.afvergani.backendtest.model.Product;

import java.util.List;

public interface ICoreService {

    List<String> getSimilarProductsIds(String requestId, String productId);

    Product getProductDetailService(String requestId, String productId);
}
