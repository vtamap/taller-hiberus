package com.bank.payment.infrastructure.outbound.persistence.adapter;

import com.bank.payment.application.port.out.PaymentOrderOutPort;
import com.bank.payment.domain.model.PaymentOrder;
import com.bank.payment.infrastructure.outbound.persistence.entity.PaymentOrderEntity;
import com.bank.payment.infrastructure.outbound.persistence.mapper.PaymentOrderPersistenceMapper;
import com.bank.payment.infrastructure.outbound.persistence.repository.JpaPaymentOrderRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PaymentOrderPersistenceAdapter implements PaymentOrderOutPort {

    private final JpaPaymentOrderRepository jpaRepository;
    private final PaymentOrderPersistenceMapper mapper;

    public PaymentOrderPersistenceAdapter(JpaPaymentOrderRepository jpaRepository,
            PaymentOrderPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<PaymentOrder> save(PaymentOrder paymentOrder) {
        PaymentOrderEntity entity = mapper.toEntity(paymentOrder);
        return jpaRepository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<PaymentOrder> findById(String paymentOrderId) {
        return jpaRepository.findByPaymentOrderId(paymentOrderId)
                .map(mapper::toDomain);
    }
}
