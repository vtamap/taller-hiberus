package com.bank.payment.application.usecase;

import com.bank.payment.application.port.in.InitiatePaymentInPort;
import com.bank.payment.application.port.out.PaymentOrderOutPort;
import com.bank.payment.domain.model.PaymentOrder;
import com.bank.payment.domain.model.PaymentStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class InitiatePaymentUseCase implements InitiatePaymentInPort {

    private final PaymentOrderOutPort paymentOrderRepository;

    public InitiatePaymentUseCase(PaymentOrderOutPort paymentOrderRepository) {
        this.paymentOrderRepository = paymentOrderRepository;
    }

    @Override
    @Transactional
    public Mono<PaymentOrder> initiate(PaymentOrder paymentOrder) {
        paymentOrder.setPaymentOrderId(UUID.randomUUID().toString());
        paymentOrder.setStatus(PaymentStatus.INITIATED);
        paymentOrder.setCreationDateTime(LocalDateTime.now());
        paymentOrder.setLastUpdate(LocalDateTime.now());
        return paymentOrderRepository.save(paymentOrder);
    }
}
