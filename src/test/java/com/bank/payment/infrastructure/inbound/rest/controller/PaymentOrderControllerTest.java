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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentOrderControllerTest {

    @Mock
    private InitiatePaymentInPort initiatePaymentUseCase;
    @Mock
    private RetrievePaymentInPort retrievePaymentUseCase;
    @Mock
    private RetrievePaymentStatusInPort retrievePaymentStatusUseCase;
    @Mock
    private PaymentOrderRestMapper mapper;

    @InjectMocks
    private PaymentOrderController controller;

    @Test
    void initiatePaymentOrder_shouldReturnCreated() {
        // Given
        PaymentOrderRequest request = new PaymentOrderRequest();
        PaymentOrder domainOrder = new PaymentOrder();
        PaymentOrderResponse response = new PaymentOrderResponse();

        when(mapper.toDomain(any(PaymentOrderRequest.class))).thenReturn(domainOrder);
        when(initiatePaymentUseCase.initiate(any(PaymentOrder.class))).thenReturn(Mono.just(domainOrder));
        when(mapper.toResponse(any(PaymentOrder.class))).thenReturn(response);

        // When
        Mono<ResponseEntity<PaymentOrderResponse>> result = controller.initiatePaymentOrder(Mono.just(request), null);

        // Then
        StepVerifier.create(result)
                .assertNext(entity -> {
                    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(entity.getBody()).isEqualTo(response);
                })
                .verifyComplete();
    }

    @Test
    void retrievePaymentOrder_shouldReturnOk_whenFound() {
        // Given
        String id = "123";
        PaymentOrder domainOrder = new PaymentOrder();
        PaymentOrderDetails details = new PaymentOrderDetails();

        when(retrievePaymentUseCase.getPayment(id)).thenReturn(Mono.just(domainOrder));
        when(mapper.toDetails(domainOrder)).thenReturn(details);

        // When
        Mono<ResponseEntity<PaymentOrderDetails>> result = controller.retrievePaymentOrder(id, null);

        // Then
        StepVerifier.create(result)
                .assertNext(entity -> {
                    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(entity.getBody()).isEqualTo(details);
                })
                .verifyComplete();
    }

    @Test
    void retrievePaymentOrder_shouldReturnNotFound_whenEmpty() {
        // Given
        String id = "123";
        when(retrievePaymentUseCase.getPayment(id)).thenReturn(Mono.empty());

        // When
        Mono<ResponseEntity<PaymentOrderDetails>> result = controller.retrievePaymentOrder(id, null);

        // Then
        StepVerifier.create(result)
                .assertNext(entity -> assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void retrievePaymentOrderStatus_shouldReturnOk_whenFound() {
        // Given
        String id = "123";
        PaymentOrder domainOrder = new PaymentOrder();
        PaymentOrderStatus status = new PaymentOrderStatus();

        when(retrievePaymentUseCase.getPayment(id)).thenReturn(Mono.just(domainOrder));
        when(mapper.toStatusDtoWrapper(domainOrder)).thenReturn(status);

        // When
        Mono<ResponseEntity<PaymentOrderStatus>> result = controller.retrievePaymentOrderStatus(id, null);

        // Then
        StepVerifier.create(result)
                .assertNext(entity -> {
                    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(entity.getBody()).isEqualTo(status);
                })
                .verifyComplete();
    }

    @Test
    void retrievePaymentOrderStatus_shouldReturnNotFound_whenEmpty() {
        // Given
        String id = "123";
        when(retrievePaymentUseCase.getPayment(id)).thenReturn(Mono.empty());

        // When
        Mono<ResponseEntity<PaymentOrderStatus>> result = controller.retrievePaymentOrderStatus(id, null);

        // Then
        StepVerifier.create(result)
                .assertNext(entity -> assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }
}
