package com.bank.payment.application.usecase;

import com.bank.payment.application.port.out.PaymentOrderOutPort;
import com.bank.payment.domain.model.PaymentOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrievePaymentUseCaseTest {

    @Mock
    private PaymentOrderOutPort paymentOrderRepository;

    @InjectMocks
    private RetrievePaymentUseCase useCase;

    @Test
    void getPayment_shouldReturnOrder_whenFound() {
        // Given
        String id = "123";
        PaymentOrder order = new PaymentOrder();
        order.setPaymentOrderId(id);

        when(paymentOrderRepository.findById(id)).thenReturn(Mono.just(order));

        // When
        Mono<PaymentOrder> result = useCase.getPayment(id);

        // Then
        StepVerifier.create(result)
                .assertNext(foundOrder -> assertThat(foundOrder.getPaymentOrderId()).isEqualTo(id))
                .verifyComplete();
    }

    @Test
    void getPayment_shouldReturnEmpty_whenNotFound() {
        // Given
        String id = "123";
        when(paymentOrderRepository.findById(id)).thenReturn(Mono.empty());

        // When
        Mono<PaymentOrder> result = useCase.getPayment(id);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }
}
