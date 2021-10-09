package io.seoul.helper.controller.settle.dto;

import io.seoul.helper.controller.user.dto.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SettleNeedPaidGroupByUserResponseDto {
    private UserResponseDto user;
    private Long mentorCount;
    private Long menteeCount;
    private List<SettleResponseDto> mentorSettles;
    private List<SettleResponseDto> menteeSettles;

    @Builder
    public SettleNeedPaidGroupByUserResponseDto(UserResponseDto user, Long mentorCount, Long menteeCount, List<SettleResponseDto> mentorSettles, List<SettleResponseDto> menteeSettles) {
        this.user = user;
        this.mentorCount = mentorCount;
        this.menteeCount = menteeCount;
        this.mentorSettles = mentorSettles;
        this.menteeSettles = menteeSettles;
    }
}
