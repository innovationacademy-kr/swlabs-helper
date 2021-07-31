package io.seoul.helper.domain.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
public class Score {
    @Column
    private Integer fun;

    @Column
    private Integer nice;

    @Column
    private Integer time;

    @Column
    private Integer interested;

    @Builder
    public Score(Integer fun, Integer nice, Integer time, Integer interested) {
        this.fun = fun;
        this.nice = nice;
        this.time = time;
        this.interested = interested;
    }
}