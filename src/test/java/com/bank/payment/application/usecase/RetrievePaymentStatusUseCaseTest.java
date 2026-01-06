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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrievePaymentStatusUseCaseTest {

    @Mock
    private PaymentOrderOutPort paymentOrderRepository;

    @InjectMocks
    private RetrievePaymentStatusUseCase useCase;

    @Test
    void getStatus_shouldReturnStatus_whenFound() {
        // Given
        String id = "123";
        PaymentOrder order = new PaymentOrder();
        order.setStatus(PaymentStatus.PROCESSED);

        when(paymentOrderRepository.findById(id)).thenReturn(Mono.just(order));

        // When
        Mono<PaymentStatus> result = useCase.getStatus(id);

        // Then
        StepVerifier.create(result)
                .assertNext(status -> assertThat(status).isEqualTo(PaymentStatus.PROCESSED))
                .verifyComplete();
    }

    @Test
    void getStatus_shouldReturnEmpty_whenNotFound() {
        // Given
        String id = "123";
        when(paymentOrderRepository.findById(id)).thenReturn(Mono.empty());

        // When
        Mono<PaymentStatus> result = useCase.getStatus(id);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }
}
