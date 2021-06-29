package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class TeamResponseDto {
    private Long teamId;
    private String projectName;

    private List<TeamMemberDto> members;
    private Long maxMemberCount;
    private Long currentMemberCount;

    private TeamStatus status;
    private TeamLocation location;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
