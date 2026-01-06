package com.bank.payment.application.port.out;

import com.bank.payment.domain.model.PaymentOrder;
import reactor.core.publisher.Mono;

public interface PaymentOrderOutPort {
    Mono<PaymentOrder> save(PaymentOrder paymentOrder);

    Mono<PaymentOrder> findById(String paymentOrderId);
}
