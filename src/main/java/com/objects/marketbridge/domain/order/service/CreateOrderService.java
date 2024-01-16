package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.address.repository.AddressRepository;
import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.model.Address;
import com.objects.marketbridge.domain.model.Coupon;
import com.objects.marketbridge.domain.model.Member;
import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.domain.OrderTemp;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.entity.ProdOrderDetail;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDetailDto;
import com.objects.marketbridge.domain.order.dto.CreateProdOrderDto;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import com.objects.marketbridge.global.utils.GroupingHelper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final AddressRepository addressRepository;
    private final TossPaymentConfig paymentConfig;


    @Transactional
    public CreateOrderResponse create(CreateProdOrderDto prodOrderDto, List<CreateProdOrderDetailDto> prodOrderDetailDtos) {

        // 1. ProdOrder, ProdOrderDetail 엔티티 생성
        ProdOrder prodOrder = createProdOrder(prodOrderDto);
        List<ProdOrderDetail> prodOrderDetails = createProdOrderDetails(prodOrderDetailDtos);

        orderRepository.save(prodOrder);

        // 2. 양방향관계이므로 양쪽 다 서로의 값을 집어넣어줘야함
        prodOrderDetails.forEach(prodOrder::addOrderDetail);
        prodOrderDetails.forEach(p -> p.setOrder(prodOrder));

        // 3.주문 엔티티 저장
        orderDetailRepository.saveAll(prodOrderDetails);

        Member member = memberRepository.findById(prodOrderDto.getMemberId()).orElseThrow(EntityNotFoundException::new);

        String email          = member.getEmail();
        String successUrl     = paymentConfig.getSuccessUrl();
        String failUrl        = paymentConfig.getFailUrl();

        return CreateOrderResponse.from(prodOrderDto, email, successUrl, failUrl);
    }

    @Transactional
    public CreateOrderResponse create(Long memberId, String orderNo, String paymentKey, Long totalOrderPrice) {

        // 1. ProdOrder 만들기
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        OrderTemp orderTemp = orderRepository.findOrderTempByOrderNo(orderNo);
        Long addressId = orderTemp.getAddressId();
        Address address = addressRepository.findById(addressId);
        String orderName = orderTemp.getOrderName();
        Long amount = orderTemp.getAmount();
        String product = orderTemp.getProduct();

        return null;
    }

    private ProdOrder createProdOrder(CreateProdOrderDto dto) {

        Member member         = memberRepository.findById(dto.getMemberId()).orElseThrow(IllegalArgumentException::new);
        Address address       = addressRepository.findById(dto.getAddressId());
        Long totalOrderPrice  = dto.getTotalOrderPrice();
        String orderName      = dto.getOrderName();
        String orderNo        = dto.getOrderNo();

        return ProdOrder.create(member, address, orderName, orderNo, totalOrderPrice);

    }

    private List<ProdOrderDetail> createProdOrderDetails(List<CreateProdOrderDetailDto> dtos) {

        // request 에서 Product 를 추출하여 Map 으로 그룹핑
        Map<Long, Product> productMap = GroupingHelper.groupingByKey(getAllProducts(dtos), Product::getId);

        // request 에서 Coupon 을 추출하여 Map 으로 그룹핑
        Map<Long, Coupon> couponMap = GroupingHelper.groupingByKey(getAllCoupons(dtos), Coupon::getId);

        return  dtos.stream()
                .map(d ->
                        ProdOrderDetail.create(
                                productMap.get(d.getProductId()),
                                couponMap.get(d.getUsedCouponId()),
                                d.getQuantity(),
                                d.getUnitOrderPrice(),
                                StatusCodeType.ORDER_INIT.getCode()
                        )
                ).toList();
    }

    private List<Coupon> getAllCoupons(List<CreateProdOrderDetailDto> dtos) {
        return couponRepository.findAllByIds(extractCouponIds(dtos));
    }

    private List<Product> getAllProducts(List<CreateProdOrderDetailDto> dtos) {
        return productRepository.findAllById(extractProductIds(dtos));
    }

    private static List<Long> extractCouponIds(List<CreateProdOrderDetailDto> dtos) {
        return dtos.stream()
                .map(CreateProdOrderDetailDto::getUsedCouponId)
                .toList();
    }

    private static List<Long> extractProductIds(List<CreateProdOrderDetailDto> dtos) {
        return dtos.stream()
                .map(CreateProdOrderDetailDto::getProductId)
                .toList();
    }
}
