package io.seoul.helper.controller.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamCountResponseDto {
    private Integer createdTeamCount;
    private Integer waitTeamCount;
    private Integer readyTeamCount;
    private Integer reviewTeamCount;
    private Integer endTeamCount;

    @Builder
    public TeamCountResponseDto(Integer createdTeamCount, Integer waitTeamCount, Integer readyTeamCount, Integer reviewTeamCount, Integer endTeamCount) {
        this.createdTeamCount = createdTeamCount;
        this.waitTeamCount = waitTeamCount;
        this.readyTeamCount = readyTeamCount;
        this.reviewTeamCount = reviewTeamCount;
        this.endTeamCount = endTeamCount;
    }
}
