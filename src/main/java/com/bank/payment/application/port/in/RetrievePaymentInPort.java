package com.bank.payment.application.port.in;

import com.bank.payment.domain.model.PaymentOrder;
import reactor.core.publisher.Mono;

public interface RetrievePaymentInPort {
    Mono<PaymentOrder> getPayment(String paymentOrderId);
}
