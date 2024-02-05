package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.member.domain.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberCouponJpaRepository extends JpaRepository<MemberCoupon, Long> {

    // TODO : findByMemberIdAndCouponId 테스트 코드 작성
    @Query("SELECT mc FROM MemberCoupon mc WHERE mc.member.id = :memberId AND mc.coupon.id = :couponId")
    Optional<MemberCoupon> findByMemberIdAndCouponId(@Param("memberId") Long memberId, @Param("couponId") Long couponId);
}
