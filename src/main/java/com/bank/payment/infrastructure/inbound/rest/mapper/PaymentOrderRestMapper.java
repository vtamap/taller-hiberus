package com.bank.payment.infrastructure.inbound.rest.mapper;

import com.bank.payment.domain.model.PaymentOrder;
import com.bank.payment.domain.model.PaymentStatus;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderDetails;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderRequest;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderResponse;
import com.bank.payment.infrastructure.inbound.rest.dto.PaymentOrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentOrderRestMapper {

    @Mapping(target = "paymentOrderId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "creationDateTime", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    PaymentOrder toDomain(PaymentOrderRequest request);

    PaymentOrderResponse toResponse(PaymentOrder paymentOrder);

    PaymentOrderDetails toDetails(PaymentOrder paymentOrder);

    PaymentOrderStatus toStatusDto(PaymentStatus status, String paymentOrderId, java.time.LocalDateTime lastUpdate);

    default PaymentOrderStatus toStatusDtoWrapper(PaymentOrder paymentOrder) {
        if (paymentOrder == null)
            return null;
        return toStatusDto(paymentOrder.getStatus(), paymentOrder.getPaymentOrderId(), paymentOrder.getLastUpdate());
    }

    default java.time.OffsetDateTime map(java.time.LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.atOffset(java.time.ZoneOffset.UTC);
    }
}
