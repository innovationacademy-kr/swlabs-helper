package io.seoul.helper.controller.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewNeedSettleCountResponseDto {
    Long count;

    @Builder
    public ReviewNeedSettleCountResponseDto(Long count) {
        this.count = count;
    }
}
