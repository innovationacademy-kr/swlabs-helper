package io.seoul.helper.controller.review.dto;

import io.seoul.helper.domain.review.ReviewStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String description;
    private ScoreDto score;
    private ReviewStatus status;

    @Builder
    public ReviewResponseDto(Long id, String description, ScoreDto score, ReviewStatus status) {
        this.id = id;
        this.description = description;
        this.score = score;
        this.status = status;
    }
}
