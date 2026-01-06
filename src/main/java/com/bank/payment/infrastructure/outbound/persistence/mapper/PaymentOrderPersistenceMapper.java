package com.bank.payment.infrastructure.outbound.persistence.mapper;

import com.bank.payment.domain.model.PaymentOrder;
import com.bank.payment.infrastructure.outbound.persistence.entity.PaymentOrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentOrderPersistenceMapper {
    PaymentOrderEntity toEntity(PaymentOrder paymentOrder);

    PaymentOrder toDomain(PaymentOrderEntity entity);
}
