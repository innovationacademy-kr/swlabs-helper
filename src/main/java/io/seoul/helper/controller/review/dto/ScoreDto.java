package io.seoul.helper.controller.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ScoreDto {
    private Integer fun;
    private Integer nice;
    private Integer time;
    private Integer interested;

    @Builder
    public ScoreDto(Integer fun, Integer nice, Integer time, Integer interested) {
        this.fun = fun;
        this.nice = nice;
        this.time = time;
        this.interested = interested;
    }
}
