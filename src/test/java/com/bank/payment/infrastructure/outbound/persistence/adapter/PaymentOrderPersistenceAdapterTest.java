package com.bank.payment.infrastructure.outbound.persistence.adapter;

import com.bank.payment.domain.model.PaymentOrder;
import com.bank.payment.infrastructure.outbound.persistence.entity.PaymentOrderEntity;
import com.bank.payment.infrastructure.outbound.persistence.mapper.PaymentOrderPersistenceMapper;
import com.bank.payment.infrastructure.outbound.persistence.repository.JpaPaymentOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentOrderPersistenceAdapterTest {

    @Mock
    private JpaPaymentOrderRepository jpaRepository;
    @Mock
    private PaymentOrderPersistenceMapper mapper;

    @InjectMocks
    private PaymentOrderPersistenceAdapter adapter;

    @Test
    void save_shouldReturnSavedPaymentOrder() {
        // Given
        PaymentOrder domainOrder = new PaymentOrder();
        PaymentOrderEntity entity = new PaymentOrderEntity();
        PaymentOrder savedDomainOrder = new PaymentOrder();

        when(mapper.toEntity(domainOrder)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(savedDomainOrder);

        // When
        Mono<PaymentOrder> result = adapter.save(domainOrder);

        // Then
        StepVerifier.create(result)
                .assertNext(order -> assertThat(order).isEqualTo(savedDomainOrder))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnPaymentOrder_whenFound() {
        // Given
        String id = "123";
        PaymentOrderEntity entity = new PaymentOrderEntity();
        PaymentOrder domainOrder = new PaymentOrder();

        when(jpaRepository.findByPaymentOrderId(id)).thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity)).thenReturn(domainOrder);

        // When
        Mono<PaymentOrder> result = adapter.findById(id);

        // Then
        StepVerifier.create(result)
                .assertNext(order -> assertThat(order).isEqualTo(domainOrder))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        // Given
        String id = "123";
        when(jpaRepository.findByPaymentOrderId(id)).thenReturn(Mono.empty());

        // When
        Mono<PaymentOrder> result = adapter.findById(id);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }
}
