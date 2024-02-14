package com.objects.marketbridge.order.mock;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.payment.service.dto.PaymentDto;
import com.objects.marketbridge.payment.service.port.PaymentClient;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import lombok.Builder;

public class FakePaymentClient implements PaymentClient {

    private DateTimeHolder dateTimeHolder;

    @Builder
    public FakePaymentClient(DateTimeHolder dateTimeHolder) {
        this.dateTimeHolder = dateTimeHolder;
    }

    @Override
    public RefundDto refund(String tid, Integer cancelAmount) {
        return RefundDto.builder()
                .totalRefundAmount(Long.valueOf(cancelAmount))
                .refundMethod("카드")
                .refundProcessedAt(dateTimeHolder.getTimeNow())
                .build();
    }

    @Override
    public PaymentDto payment(Order order, String pgToken) {
        return null;
    }

}