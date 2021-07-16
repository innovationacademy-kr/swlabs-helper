package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.project.Project;
import io.seoul.helper.domain.team.Period;
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
    private Long projectId;
    private String description;
    private MemberRole memberRole;

    @Builder
    public TeamCreateRequestDto(LocalDateTime startTime, LocalDateTime endTime, TeamLocation location,
                                Long maxMemberCount, Long projectId, String description, MemberRole memberRole) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxMemberCount = maxMemberCount;
        this.projectId = projectId;
        this.description = description;
        this.memberRole = memberRole;
    }

    public Team toEntity(Project project, TeamStatus teamStatus) {
        Period period = Period.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
        Team entity = Team.builder()
                .period(period)
                .location(location)
                .maxMemberCount(maxMemberCount)
                .status(teamStatus)
                .maxMemberCount(maxMemberCount)
                .project(project)
                .description(description)
                .build();
        return entity;
    }
}
