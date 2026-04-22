package com.goylik.payment_service.service;

import com.goylik.payment_service.model.enums.PaymentStatus;
import com.goylik.payment_service.service.impl.PaymentProcessingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentProcessingServiceTest {
    @Mock private RestClient restClient;
    @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private PaymentProcessingServiceImpl paymentProcessingService;

    @Test
    void pay_ShouldReturnSuccess_WhenRandomNumberIsEven() {
        mockRestClient("42");

        PaymentStatus result = paymentProcessingService.pay();

        assertEquals(PaymentStatus.SUCCESS, result);
    }

    @Test
    void pay_ShouldReturnFailed_WhenRandomNumberIsOdd() {
        mockRestClient("13");

        PaymentStatus result = paymentProcessingService.pay();

        assertEquals(PaymentStatus.FAILED, result);
    }

    @Test
    void pay_ShouldReturnFailed_WhenResponseIsNotANumber() {
        mockRestClient("not-a-number");

        PaymentStatus result = paymentProcessingService.pay();

        assertEquals(PaymentStatus.FAILED, result);
    }

    @Test
    void pay_ShouldReturnFailed_WhenRestClientThrowsException() {
        when(restClient.get()).thenThrow(new RuntimeException("Connection refused"));

        PaymentStatus result = paymentProcessingService.pay();

        assertEquals(PaymentStatus.FAILED, result);
    }

    @Test
    void pay_ShouldReturnFailed_WhenResponseIsNull() {
        mockRestClient(null);

        PaymentStatus result = paymentProcessingService.pay();

        assertEquals(PaymentStatus.FAILED, result);
    }

    @Test
    void pay_ShouldReturnSuccess_WhenBoundaryEvenNumber() {
        mockRestClient("100");

        PaymentStatus result = paymentProcessingService.pay();

        assertEquals(PaymentStatus.SUCCESS, result);
    }

    @Test
    void pay_ShouldReturnFailed_WhenBoundaryOddNumber() {
        mockRestClient("1");

        PaymentStatus result = paymentProcessingService.pay();

        assertEquals(PaymentStatus.FAILED, result);
    }

    private void mockRestClient(String response) {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(response);
    }
}
