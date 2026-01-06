package com.bank.payment.application.usecase;

import com.bank.payment.application.port.out.PaymentOrderOutPort;
import com.bank.payment.domain.model.PaymentOrder;
import com.bank.payment.domain.model.PaymentStatus;
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
class InitiatePaymentUseCaseTest {

    @Mock
    private PaymentOrderOutPort paymentOrderRepository;

    @InjectMocks
    private InitiatePaymentUseCase useCase;

    @Test
    void initiate_shouldSetDefaultsAndSave() {
        // Given
        PaymentOrder order = new PaymentOrder();

        when(paymentOrderRepository.save(any(PaymentOrder.class))).thenAnswer(invocation -> {
            PaymentOrder savedOrder = invocation.getArgument(0);
            return Mono.just(savedOrder);
        });

        // When
        Mono<PaymentOrder> result = useCase.initiate(order);

        // Then
        StepVerifier.create(result)
                .assertNext(savedOrder -> {
                    assertThat(savedOrder.getPaymentOrderId()).isNotNull();
                    assertThat(savedOrder.getStatus()).isEqualTo(PaymentStatus.INITIATED);
                    assertThat(savedOrder.getCreationDateTime()).isNotNull();
                    assertThat(savedOrder.getLastUpdate()).isNotNull();
                })
                .verifyComplete();
    }
}
