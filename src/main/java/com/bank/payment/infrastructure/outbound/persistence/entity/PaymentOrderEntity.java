package com.bank.payment.infrastructure.outbound.persistence.entity;

import com.bank.payment.domain.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("payment_orders")
public class PaymentOrderEntity {

    @Id
    private Long id;

    private String paymentOrderId;
    private String externalId;
    private String debtorIban;
    private String creditorIban;
    private BigDecimal amount;
    private String currency;
    private String remittanceInfo;
    private LocalDate requestedExecutionDate;
    private PaymentStatus status;
    private LocalDateTime creationDateTime;
    private LocalDateTime lastUpdate;
}
