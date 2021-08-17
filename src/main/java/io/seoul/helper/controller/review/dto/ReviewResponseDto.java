package io.seoul.helper.controller.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String description;
    private ScoreDto score;

    @Builder
    public ReviewResponseDto(Long id, String description, ScoreDto score) {
        this.id = id;
        this.description = description;
        this.score = score;
    }
}
