package com.afvergani.backendtest.core.service;

import com.afvergani.backendtest.model.Product;
import com.afvergani.backendtest.service.ProductService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ICoreService coreService;

    @BeforeEach
    public void setup() {
        coreService = mock(ICoreService.class);
        productService = new ProductService(coreService);
    }

    @Test
    public void testGetSimilarProducts_SuccessfulRequest() {
        List<String> similarProductIds = List.of("1", "2", "3");
        String requestId = "123";
        String productId = "4";
        when(coreService.getSimilarProductsIds("123", "4")).thenReturn(similarProductIds);

        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product("1", "Product 1", 10.0,false));
        expectedProducts.add(new Product("2", "Product 2", 20.0, true));
        expectedProducts.add(new Product("3", "Product 3", 30.0,true));

        when(coreService.getProductDetailService(requestId, "1")).thenReturn(new Product("1", "Product 1", 10.0,false));
        when(coreService.getProductDetailService(requestId, "2")).thenReturn(new Product("2", "Product 2", 20.0,true));
        when(coreService.getProductDetailService(requestId, "3")).thenReturn(new Product("3", "Product 3", 30.0,true));

        ResponseEntity<?> responseEntity = productService.getSimilarProducts(requestId, productId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedProducts, responseEntity.getBody());
    }

    @Test
    public void testGetSimilarProducts_NullSimilarProductIds() {
        String requestId = "123";
        String productId = "4";

        when(coreService.getSimilarProductsIds(requestId, productId)).thenReturn(null);
        ResponseEntity<?> responseEntity = productService.getSimilarProducts(requestId, productId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("",responseEntity.getBody());
    }

    @Test
    public void testGetSimilarProducts_Exception() {
        String requestId = "123";
        String productId = "4";

        when(coreService.getSimilarProductsIds(requestId, productId)).thenThrow(new RuntimeException("Error"));
        ResponseEntity<?> responseEntity = productService.getSimilarProducts(requestId, productId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("", responseEntity.getBody());
    }
}
