package io.seoul.helper.controller.settle.dto;

import io.seoul.helper.controller.review.dto.ReviewResponseDto;
import io.seoul.helper.controller.user.dto.UserResponseDto;
import io.seoul.helper.domain.settle.SettleStatus;
import lombok.Builder;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
public class SettleResponseDto {
    @JsonIgnore
    private Long id;
    private ReviewResponseDto review;
    private UserResponseDto admin;
    private SettleStatus status;

    @Builder
    public SettleResponseDto(Long id, ReviewResponseDto review, UserResponseDto admin, SettleStatus status) {
        this.id = id;
        this.review = review;
        this.admin = admin;
        this.status = status;
    }
}
