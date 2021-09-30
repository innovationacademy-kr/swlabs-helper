package io.seoul.helper.controller.team.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamDateDto {
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;

    @Builder
    public TeamDateDto(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
