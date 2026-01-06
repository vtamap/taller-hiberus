package com.bank.payment.application.usecase;

import com.bank.payment.application.port.in.RetrievePaymentInPort;
import com.bank.payment.application.port.out.PaymentOrderOutPort;
import com.bank.payment.domain.model.PaymentOrder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
public class RetrievePaymentUseCase implements RetrievePaymentInPort {

    private final PaymentOrderOutPort paymentOrderRepository;

    public RetrievePaymentUseCase(PaymentOrderOutPort paymentOrderRepository) {
        this.paymentOrderRepository = paymentOrderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PaymentOrder> getPayment(String paymentOrderId) {
        return paymentOrderRepository.findById(paymentOrderId);
    }
}
