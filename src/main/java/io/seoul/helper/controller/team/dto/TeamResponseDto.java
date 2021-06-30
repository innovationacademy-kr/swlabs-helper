package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TeamResponseDto {
    private Long teamId;
    private Long maxMemberCount;
    private Long currentMemberCount;
    private String projectName;

    private TeamStatus status;
    private TeamLocation location;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<TeamMemberDto> members;

    public TeamResponseDto(Team team) {
        this.teamId = team.getId();
        this.projectName = team.getProject().getName();
        this.maxMemberCount = team.getMaxMemberCount();
        this.currentMemberCount = team.getMaxMemberCount() - team.getMembers().size();
        this.status = team.getStatus();
        this.location = team.getLocation();
        this.startTime = team.getStartTime();
        this.endTime = team.getEndTime();
        this.members = team.getMembers().stream()
                .map(member -> new TeamMemberDto(member))
                .collect(Collectors.toList());
    }
}
