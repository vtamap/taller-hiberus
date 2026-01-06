package com.bank.payment.application.port.in;

import com.bank.payment.domain.model.PaymentOrder;
import reactor.core.publisher.Mono;

public interface InitiatePaymentInPort {
    Mono<PaymentOrder> initiate(PaymentOrder paymentOrder);
}
