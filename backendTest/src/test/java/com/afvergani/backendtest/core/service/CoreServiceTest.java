package com.afvergani.backendtest.core.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@PropertySource("classpath:application.properties")
public class CoreServiceTest {

    private CoreService coreService;
    private HttpClient httpClient;
    private HttpResponse<String> successResponse;
    private HttpResponse<String> errorResponse;

    @BeforeEach
    public void setup() {
        coreService = new CoreService();
        httpClient = Mockito.mock(HttpClient.class);
        coreService.setHttpClient(httpClient);

        // Configurar respuestas simuladas
        successResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(successResponse.statusCode()).thenReturn(200);
        Mockito.when(successResponse.body()).thenReturn("[\"1\",\"2\",\"3\"]");

        errorResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(errorResponse.statusCode()).thenReturn(404);
    }

    @Test
    public void testGetSimilarProductsIds_Success() throws Exception {
        // Configurar el comportamiento simulado del cliente HTTP
        Mockito.when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(successResponse);

        // Ejecutar el método bajo prueba
        List<String> result = coreService.getSimilarProductsIds("requestId", "productId");

        // Verificar el resultado
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.contains("1"));
        Assertions.assertTrue(result.contains("2"));
        Assertions.assertTrue(result.contains("3"));
    }

    @Test
    public void testGetSimilarProductsIds_Error() throws Exception {
        // Configurar el comportamiento simulado del cliente HTTP
        Mockito.when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(errorResponse);

        // Ejecutar el método bajo prueba
        List<String> result = coreService.getSimilarProductsIds("requestId", "productId");

        // Verificar el resultado
        Assertions.assertNull(result);
    }

    @Test
    public void testGetSimilarProductsIds_Exception() throws Exception {
        // Configurar el comportamiento simulado del cliente HTTP para lanzar una excepción
        Mockito.when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Error al enviar la solicitud HTTP"));

        // Ejecutar el método bajo prueba
        List<String> result = coreService.getSimilarProductsIds("requestId", "productId");

        // Verificar el resultado
        Assertions.assertNull(result);
    }
}
