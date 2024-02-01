package com.objects.marketbridge.order.controller.request;

import com.objects.marketbridge.order.service.dto.CancelRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderCancelRequestTest {

    @Test
    @DisplayName("serviceDto로 변환할 수 있다.")
    public void toServiceRequest() {
        // given
        OrderCancelRequest orderCancelRequest = OrderCancelRequest.builder()
                .orderNo("1")
                .cancelReason("옥지보단 빵빵이")
                .build();

        // when
        CancelRequestDto result = orderCancelRequest.toServiceRequest();

        // then
        Assertions.assertThat(result).extracting("orderNo", "cancelReason")
                .contains("1", "옥지보단 빵빵이");
    }
}