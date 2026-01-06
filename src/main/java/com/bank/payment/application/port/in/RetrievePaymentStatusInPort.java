package com.bank.payment.application.port.in;

import com.bank.payment.domain.model.PaymentStatus;
import reactor.core.publisher.Mono;

public interface RetrievePaymentStatusInPort {
    Mono<PaymentStatus> getStatus(String paymentOrderId);
}
