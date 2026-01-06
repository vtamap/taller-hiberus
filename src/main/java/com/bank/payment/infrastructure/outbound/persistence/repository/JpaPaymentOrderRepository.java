package com.bank.payment.infrastructure.outbound.persistence.repository;

import com.bank.payment.infrastructure.outbound.persistence.entity.PaymentOrderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface JpaPaymentOrderRepository extends ReactiveCrudRepository<PaymentOrderEntity, Long> {
    Mono<PaymentOrderEntity> findByPaymentOrderId(String paymentOrderId);
}
