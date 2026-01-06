package com.bank.payment.infrastructure.inbound.rest.controller;

import com.bank.payment.application.port.in.InitiatePaymentInPort;
import com.bank.payment.application.port.in.RetrievePaymentInPort;
import com.bank.payment.application.port.in.RetrievePaymentStatusInPort;
import com.bank.payment.domain.model.PaymentOrder;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderDetails;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderRequest;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderResponse;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderStatus;
import com.bank.payment.infrastructure.inbound.rest.mapper.PaymentOrderRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(PaymentOrderController.class)
class PaymentOrderControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private InitiatePaymentInPort initiatePaymentUseCase;
    @MockBean
    private RetrievePaymentInPort retrievePaymentUseCase;
    @MockBean
    private RetrievePaymentStatusInPort retrievePaymentStatusUseCase;
    @MockBean
    private PaymentOrderRestMapper mapper;

    @Test
    void initiatePaymentOrder_shouldReturnCreated() {
        PaymentOrderRequest request = new PaymentOrderRequest();
        request.setExternalId("ext-123");
        request.setDebtorIban("ES1212345678901234567890");
        request.setCreditorIban("ES1298765432109876543210");
        request.setAmount(100.0);
        request.setCurrency("EUR");
        request.setRequestedExecutionDate(java.time.LocalDate.now());
        PaymentOrderResponse response = new PaymentOrderResponse();

        when(mapper.toDomain(any(PaymentOrderRequest.class))).thenReturn(new PaymentOrder());
        when(initiatePaymentUseCase.initiate(any(PaymentOrder.class))).thenReturn(Mono.just(new PaymentOrder()));
        when(mapper.toResponse(any(PaymentOrder.class))).thenReturn(response);

        webTestClient.post()
                .uri("/payment-initiation/payment-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PaymentOrderResponse.class);
    }

    @Test
    void retrievePaymentOrder_shouldReturnOk() {
        String id = "123";
        PaymentOrderDetails details = new PaymentOrderDetails();

        when(retrievePaymentUseCase.getPayment(id)).thenReturn(Mono.just(new PaymentOrder()));
        when(mapper.toDetails(any(PaymentOrder.class))).thenReturn(details);

        webTestClient.get()
                .uri("/payment-initiation/payment-orders/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentOrderDetails.class);
    }

    @Test
    void retrievePaymentOrderStatus_shouldReturnOk() {
        String id = "123";
        PaymentOrderStatus status = new PaymentOrderStatus();

        when(retrievePaymentUseCase.getPayment(id)).thenReturn(Mono.just(new PaymentOrder()));
        when(mapper.toStatusDtoWrapper(any(PaymentOrder.class))).thenReturn(status);

        webTestClient.get()
                .uri("/payment-initiation/payment-orders/{id}/status", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentOrderStatus.class);
    }
}
