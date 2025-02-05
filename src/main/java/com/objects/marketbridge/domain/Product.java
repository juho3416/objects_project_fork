package com.objects.marketbridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    // TODO
    private Long categoryId;

    private boolean isOwn; // 로켓 true , 오픈 마켓 false

    private String name;

    private Integer price;

    private boolean isSubs;

    private String thumbImg;

    private Integer discountRate;

    @Builder
    private Product(Long categoryId, boolean isOwn, String name, Integer price, boolean isSubs, String thumbImg, Integer discountRate) {
        this.categoryId = categoryId;
        this.isOwn = isOwn;
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
    }
}
