package com.objects.marketbridge.payment.service;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuitPaymentService {

    private final OrderCommendRepository orderCommendRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final DateTimeHolder dateTimeHolder;

    @Transactional
    public void cancel(String orderNo) {
        Order order = orderQueryRepository.findByOrderNo(orderNo);
        // 1) 사용했던 쿠폰들 다시 되돌리기
        order.changeMemberCouponInfo(dateTimeHolder);

        // 2) 감소했던 재고 다시 되돌리기
        order.stockIncrease();

        // 3) 주문, 상세주문들 softDelete
        orderCommendRepository.deleteByOrderNo(orderNo);
    }
}
