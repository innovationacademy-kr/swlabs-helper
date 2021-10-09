package io.seoul.helper.controller.settle.dto;

import io.seoul.helper.domain.settle.SettleStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SettlePostRequestDto {
    private Long reviewId;
    private SettleStatus status;

    @Builder
    public SettlePostRequestDto(Long reviewId, SettleStatus status) {
        this.reviewId = reviewId;
        this.status = status;
    }
}
