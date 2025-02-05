package com.objects.marketbridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EstimatedTime extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "estimated_time_id")
    private Long id;

    private Integer hour;

    private Integer addDay;

    @Builder
    private EstimatedTime(Integer hour, Integer addDay) {
        this.hour = hour;
        this.addDay = addDay;
    }
}
