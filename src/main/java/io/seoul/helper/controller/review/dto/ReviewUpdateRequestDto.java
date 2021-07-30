package io.seoul.helper.controller.review.dto;

import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.review.Score;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewUpdateRequestDto {
    private Long id;
    private String description;
    private ScoreDto score;

    @Builder
    public ReviewUpdateRequestDto(Long id, String description, ScoreDto score) {
        this.id = id;
        this.description = description;
        this.score = score;
    }

    public Review toEntitiy() {
        return Review.builder()
                .description(description)
                .score(Score.builder()
                        .nice(score.getNice())
                        .fun(score.getFun())
                        .time(score.getTime())
                        .interested(score.getInterested())
                        .build())
                .build();
    }
}
