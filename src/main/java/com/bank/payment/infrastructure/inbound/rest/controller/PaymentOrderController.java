package com.bank.payment.infrastructure.inbound.rest.controller;

import com.bank.payment.application.port.in.InitiatePaymentInPort;
import com.bank.payment.application.port.in.RetrievePaymentStatusInPort;
import com.bank.payment.application.port.in.RetrievePaymentInPort;
import com.bank.payment.infrastructure.inbound.rest.api.PaymentInitiationApi;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderDetails;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderRequest;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderResponse;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderStatus;
import com.bank.payment.infrastructure.inbound.rest.mapper.PaymentOrderRestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class PaymentOrderController implements PaymentInitiationApi {

    private final InitiatePaymentInPort initiatePaymentUseCase;
    private final RetrievePaymentInPort retrievePaymentUseCase;
    private final RetrievePaymentStatusInPort retrievePaymentStatusUseCase;
    private final PaymentOrderRestMapper mapper;

    public PaymentOrderController(InitiatePaymentInPort initiatePaymentUseCase,
            RetrievePaymentInPort retrievePaymentUseCase,
            RetrievePaymentStatusInPort retrievePaymentStatusUseCase,
            PaymentOrderRestMapper mapper) {
        this.initiatePaymentUseCase = initiatePaymentUseCase;
        this.retrievePaymentUseCase = retrievePaymentUseCase;
        this.retrievePaymentStatusUseCase = retrievePaymentStatusUseCase;
        this.mapper = mapper;
    }

    @Override
    @PostMapping("/payment-initiation/payment-orders")
    public Mono<ResponseEntity<PaymentOrderResponse>> initiatePaymentOrder(
            Mono<PaymentOrderRequest> paymentOrderRequest, ServerWebExchange exchange) {
        return paymentOrderRequest
                .map(mapper::toDomain)
                .flatMap(initiatePaymentUseCase::initiate)
                .map(mapper::toResponse)
                .map(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
    }

    @Override
    @GetMapping("/payment-initiation/payment-orders/{paymentOrderId}")
    public Mono<ResponseEntity<PaymentOrderDetails>> retrievePaymentOrder(String paymentOrderId,
            ServerWebExchange exchange) {
        return retrievePaymentUseCase.getPayment(paymentOrderId)
                .map(mapper::toDetails)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping("/payment-initiation/payment-orders/{paymentOrderId}/status")
    public Mono<ResponseEntity<PaymentOrderStatus>> retrievePaymentOrderStatus(String paymentOrderId,
            ServerWebExchange exchange) {
        // We need the full order to get the lastUpdate time, not just the status enum
        return retrievePaymentUseCase.getPayment(paymentOrderId)
                .map(mapper::toStatusDtoWrapper)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
