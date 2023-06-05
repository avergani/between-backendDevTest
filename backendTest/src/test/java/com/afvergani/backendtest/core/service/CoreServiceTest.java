package com.afvergani.backendtest.core.service;

import com.afvergani.backendtest.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(properties = {"get.similar.products.id.url=http://localhost:3001/product","get.product.detail.url=http://localhost:3001/product"})
public class CoreServiceTest {

    @Mock
    private HttpClient httpClient;

    @Autowired
    private CoreService coreService;

    @BeforeEach
    public void setup() {
        coreService = new CoreService();
        coreService.httpClient = httpClient;

    }

    @Test
    public void testGetSimilarProductsIds_SuccessfulRequest() throws Exception {
        String requestId = "123";
        String productId = "4";
        String responseJson = "[1,2,3]";
        ReflectionTestUtils.setField(coreService, "similarProductIdUrl", "http://localhost:3001/product");
        URI expectedUri = new URI("http://localhost:3001/product/4/similarids");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(expectedUri)
                .GET()
                .build();


        HttpResponse response = Mockito.mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(responseJson);

        when(httpClient.send(Mockito.eq(request), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        List<String> similarProductsIds = coreService.getSimilarProductsIds(requestId, productId);

        verify(httpClient).send(Mockito.eq(request), Mockito.any(HttpResponse.BodyHandler.class));
        assertEquals(List.of("1", "2", "3"), similarProductsIds);
    }

    @Test
    public void testGetSimilarProductsIds_RequestFailed() throws Exception {
        String requestId = "123";
        String productId = "4";
        ReflectionTestUtils.setField(coreService, "similarProductIdUrl", "http://localhost:3001/product");
        URI expectedUri = new URI("http://localhost:3001/product/4/similarids");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(expectedUri)
                .GET()
                .build();

        HttpResponse response = Mockito.mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(404);

        when(httpClient.send(Mockito.eq(request), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);


        List<String> similarProductsIds = coreService.getSimilarProductsIds(requestId, productId);

        verify(httpClient).send(request, HttpResponse.BodyHandlers.ofString());
        assertNull(similarProductsIds);
    }

    @Test
    public void testGetProductDetailService_SuccessfulRequest() throws Exception {
        String requestId = "123";
        String productId = "4";
        String responseJson = "{\"id\":\"4\",\"name\":\"Product Name\",\"price\":9.99,\"availability\":true}";
        ReflectionTestUtils.setField(coreService, "productDetailUrl", "http://localhost:3001/product");
        URI expectedUri = new URI("http://localhost:3001/product/4");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(expectedUri)
                .GET()
                .build();

        HttpResponse response = Mockito.mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(responseJson);

        when(httpClient.send(Mockito.eq(request), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        Product product = coreService.getProductDetailService(requestId, productId);

        verify(httpClient).send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("4", product.getId());
        assertEquals("Product Name", product.getName());
        assertEquals(9.99, product.getPrice(), 0.001);
        assertTrue(product.isAvailability());
    }

    @Test
    public void testGetProductDetailService_RequestFailed() throws Exception {
        String requestId = "123";
        String productId = "4";
        ReflectionTestUtils.setField(coreService, "productDetailUrl", "http://localhost:3001/product");

        URI expectedUri = new URI("http://localhost:3001/product/4");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(expectedUri)
                .GET()
                .build();

        HttpResponse response = Mockito.mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(404);

        when(httpClient.send(request, HttpResponse.BodyHandlers.ofString())).thenReturn(response);

        Product product = coreService.getProductDetailService(requestId, productId);

        verify(httpClient).send(request, HttpResponse.BodyHandlers.ofString());
        assertNull(product);
    }
}
