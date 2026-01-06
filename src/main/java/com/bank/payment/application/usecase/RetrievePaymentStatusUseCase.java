package com.bank.payment.application.usecase;

import com.bank.payment.application.port.in.RetrievePaymentStatusInPort;
import com.bank.payment.application.port.out.PaymentOrderOutPort;
import com.bank.payment.domain.model.PaymentOrder;
import com.bank.payment.domain.model.PaymentStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
public class RetrievePaymentStatusUseCase implements RetrievePaymentStatusInPort {

    private final PaymentOrderOutPort paymentOrderRepository;

    public RetrievePaymentStatusUseCase(PaymentOrderOutPort paymentOrderRepository) {
        this.paymentOrderRepository = paymentOrderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PaymentStatus> getStatus(String paymentOrderId) {
        return paymentOrderRepository.findById(paymentOrderId)
                .map(PaymentOrder::getStatus);
    }
}
