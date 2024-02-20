package com.objects.marketbridge.member.infra.wishlist;

import com.objects.marketbridge.member.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishiListJpaRepository extends JpaRepository<Wishlist,Long> {

    @Query("SELECT w FROM Wishlist w WHERE w.member.id = :memberId")
    List<Wishlist> findByMemberId(@Param("memberId") Long memberId);

    void deleteByMemberIdAndProductId(Long memberId, Long productId);
}