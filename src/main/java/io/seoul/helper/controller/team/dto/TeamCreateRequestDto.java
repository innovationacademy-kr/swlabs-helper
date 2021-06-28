package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.project.Project;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TeamCreateRequestDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private TeamLocation location;
    private Long maxMemeberCount;
    private String projectName;

    @Builder
    public TeamCreateRequestDto(LocalDateTime startTime, LocalDateTime endTime, TeamLocation location,
                                Long maxMemeberCount, String projectName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxMemeberCount = maxMemeberCount;
        this.projectName = projectName;
    }

    public Team toEntity(Project project) {
        Team entity = Team.builder().startTime(startTime)
                .endTime(endTime)
                .location(location)
                .maxMemberCount(maxMemeberCount)
                .status(TeamStatus.WAITING)
                .maxMemberCount(maxMemeberCount)
                .project(project)
                .build();
        return entity;
    }
}
