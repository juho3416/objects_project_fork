package com.objects.marketbridge.review.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReviewDto {

    private Long productId;
    private Integer rating; //별점, 1~5
    private List<CreateReviewSurveyDto> reviewSurveys = new ArrayList<>();
    private String content;
    private List<ReviewImageDto> reviewImgUrls = new ArrayList<>();
    private String summary;

    @Builder
    public CreateReviewDto(Long productId,
                           Integer rating,
                           List<CreateReviewSurveyDto> reviewSurveys,
                           String content,
                           List<ReviewImageDto> reviewImgUrls,
                           String summary) {
        this.productId = productId;
        this.rating = rating;
        this.reviewSurveys = reviewSurveys;
        this.content = content;
        this.reviewImgUrls = reviewImgUrls;
        this.summary = summary;
    }
}
