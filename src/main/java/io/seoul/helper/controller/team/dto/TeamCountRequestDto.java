package io.seoul.helper.controller.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TeamCountRequestDto {
    private LocalDateTime start;
    private LocalDateTime end;

    @Builder
    public TeamCountRequestDto(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }
}
