package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.MembershipType;
import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.objects.marketbridge.order.domain.MemberShipPrice.BASIC;
import static com.objects.marketbridge.order.domain.MemberShipPrice.WOW;

public class GetCancelReturnDetailDto {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private LocalDateTime orderDate;
        private LocalDateTime cancelDate;
        private String orderNo;
        private String cancelReason;
        private List<ProductInfo> productListResponseDtos;
        private CancelRefundInfoResponseDto cancelRefundInfoResponseDto;

        @Builder
        private Response(LocalDateTime orderDate, LocalDateTime cancelDate, String orderNo, String cancelReason, List<ProductInfo> productListResponseDtos, CancelRefundInfoResponseDto cancelRefundInfoResponseDto) {
            this.orderDate = orderDate;
            this.cancelDate = cancelDate;
            this.orderNo = orderNo;
            this.cancelReason = cancelReason;
            this.productListResponseDtos = productListResponseDtos;
            this.cancelRefundInfoResponseDto = cancelRefundInfoResponseDto;
        }

        public static Response of(Order order, List<OrderDetail> orderDetails, String memberShip, DateTimeHolder dateTimeHolder) {
            return Response.builder()
                    .orderDate(dateTimeHolder.getCreateTime(order))
                    .cancelDate(dateTimeHolder.getUpdateTime(order))
                    .orderNo(order.getOrderNo())
                    .cancelReason(orderDetails.get(0).getReason())
                    .productListResponseDtos(
                            orderDetails.stream()
                                    .map(ProductInfo::of)
                                    .toList()
                    )
                    .cancelRefundInfoResponseDto(
                            CancelRefundInfoResponseDto.of(orderDetails, memberShip)
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProductInfo {
        private Long productId;
        private String productNo;
        private String name;
        private Long price;
        private Long quantity;

        @Builder
        private ProductInfo(Long productId, String productNo, String name, Long price, Long quantity) {
            this.productId = productId;
            this.productNo = productNo;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public static ProductInfo of(Product product, Long quantity) {
            return ProductInfo.builder()
                    .productId(product.getId())
                    .productNo(product.getProductNo())
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(quantity)
                    .build();
        }

        public static ProductInfo of(OrderDetail orderDetail) {
            return ProductInfo.builder()
                    .productId(orderDetail.getProduct().getId())
                    .productNo(orderDetail.getProduct().getProductNo())
                    .name(orderDetail.getProduct().getName())
                    .price(orderDetail.getProduct().getPrice())
                    .quantity(orderDetail.getQuantity())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CancelRefundInfoResponseDto {

        private Long deliveryFee;
        private Long refundFee;
        private Long discountPrice;
        private Long totalPrice;

        @Builder
        private CancelRefundInfoResponseDto(Long deliveryFee, Long refundFee, Long discountPrice, Long totalPrice) {
            this.deliveryFee = deliveryFee;
            this.refundFee = refundFee;
            this.discountPrice = discountPrice;
            this.totalPrice = totalPrice;
        }

        public static CancelRefundInfoResponseDto of(List<OrderDetail> orderDetails, String memberShip) {
            if (isBasicMember(memberShip)) {
                return createDto(orderDetails, BASIC.getDeliveryFee(), BASIC.getRefundFee());
            }
            return createDto(orderDetails, WOW.getDeliveryFee(), WOW.getRefundFee());
        }

        private static boolean isBasicMember(String memberShip) {
            return Objects.equals(memberShip, MembershipType.BASIC.getText());
        }

        private static CancelRefundInfoResponseDto createDto(List<OrderDetail> orderDetails, Long deliveryFee, Long refundFee) {
            return CancelRefundInfoResponseDto.builder()
                    .discountPrice( // TODO coupon 으로 인해 N+1문제 발생할 것으로 예상 -> fetchJoin으로 쿠폰까지 조인후 해결
                            orderDetails.stream()
                                    .mapToLong(orderDetail -> orderDetail.getCoupon().getPrice())
                                    .sum()
                    )
                    .totalPrice(
                            orderDetails.stream()
                                    .mapToLong(OrderDetail::totalAmount)
                                    .sum()
                    )
                    .deliveryFee(deliveryFee)
                    .refundFee(refundFee)
                    .build();
        }
    }
}
