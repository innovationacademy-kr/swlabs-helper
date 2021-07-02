package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.project.Project;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TeamCreateRequestDto {
    @DateTimeFormat
    private LocalDateTime startTime;
    @DateTimeFormat
    private LocalDateTime endTime;
    private TeamLocation location;
    private Long maxMemberCount;
    private String projectName;

    @Builder
    public TeamCreateRequestDto(LocalDateTime startTime, LocalDateTime endTime, TeamLocation location,
                                Long maxMemberCount, String projectName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxMemberCount = maxMemberCount;
        this.projectName = projectName;
    }

    public Team toEntity(Project project) {
        Team entity = Team.builder().startTime(startTime)
                .endTime(endTime)
                .location(location)
                .maxMemberCount(maxMemberCount)
                .status(TeamStatus.WAITING)
                .maxMemberCount(maxMemberCount)
                .project(project)
                .build();
        return entity;
    }
}
