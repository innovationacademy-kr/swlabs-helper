package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.team.TeamLocation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TeamUpdateRequestDto {
    @DateTimeFormat
    private LocalDateTime startTime;
    @DateTimeFormat
    private LocalDateTime endTime;
    private TeamLocation location;
    private Long maxMemberCount;
    private Long projectId;

    @Builder
    public TeamUpdateRequestDto(LocalDateTime startTime, LocalDateTime endTime, TeamLocation location,
                                Long maxMemberCount, Long projectId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.maxMemberCount = maxMemberCount;
        this.projectId = projectId;
    }
}
