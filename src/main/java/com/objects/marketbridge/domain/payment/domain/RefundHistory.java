package com.objects.marketbridge.domain.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;

// TODO 예시로 만들어 둠
@Entity
public class RefundHistory {
    
    @Id @GeneratedValue
    private Long id;
    
    private String accountNo;

    @Builder
    private RefundHistory(String accountNo) {
        this.accountNo = accountNo;
    }
}
