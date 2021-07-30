package io.seoul.helper.controller.review.dto;

import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.review.Score;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewUpdateRequestDto {
    private Long id;
    private String description;
    private NewScore score;

    @Builder
    public ReviewUpdateRequestDto(Long id, String description, NewScore score) {
        this.id = id;
        this.description = description;
        this.score = score;
    }

    public Review toEntitiy() {
        return Review.builder()
                .description(description)
                .score(Score.builder()
                        .nice(score.nice)
                        .fun(score.fun).time(score.time)
                        .interested(score.interested)
                        .build())
                .build();
    }

    @Getter
    public static class NewScore {
        private Integer fun;
        private Integer nice;
        private Integer time;
        private Integer interested;

        @Builder
        public NewScore(Integer fun, Integer nice, Integer time, Integer interested) {
            this.fun = fun;
            this.nice = nice;
            this.time = time;
            this.interested = interested;
        }
    }
}
