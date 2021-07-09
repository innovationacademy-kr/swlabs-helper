package io.seoul.helper.controller.team.dto;

import io.seoul.helper.controller.project.dto.ProjectDto;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class TeamResponseDto {
    private Long teamId;
    private Long maxMemberCount;
    private Long currentMemberCount;
    private ProjectDto project;

    private TeamStatus status;
    private TeamLocation location;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<TeamMemberDto> members;

    public TeamResponseDto(Team team) {
        this.teamId = team.getId();
        this.project = ProjectDto.builder()
                .id(team.getProject().getId())
                .name(team.getProject().getName())
                .build();
        this.maxMemberCount = team.getMaxMemberCount();
        this.currentMemberCount = (long) team.getMembers().size();
        this.status = team.getStatus();
        this.location = team.getLocation();
        this.startTime = team.getPeriod().getStartTime();
        this.endTime = team.getPeriod().getEndTime();
        this.members = team.getMembers().stream()
                .map(member -> new TeamMemberDto(member))
                .collect(Collectors.toList());
    }

    public String getNicknameByRole(MemberRole memberRole) {
        String nickname = "empty";
        Optional<String> optional = Optional.of(nickname);

        for (TeamMemberDto member : this.members) {
            if (memberRole.getName().equals(member.getMemberRole()))
                return member.getNickname();
        }
        return optional.get();
    }

    public String getNicknameByMentee() {
        MemberRole memberRole = MemberRole.MENTEE;
        Optional<String> optional = Optional.of("empty");

        for (TeamMemberDto member : this.members) {
            if (memberRole.getName().equals(member.getMemberRole()))
                return member.getNickname();
        }
        return optional.get();
    }

    public String getNicknameByMentor() {
        MemberRole memberRole = MemberRole.MENTOR;
        Optional<String> optional = Optional.of("empty");

        for (TeamMemberDto member : this.members) {
            if (memberRole.getName().equals(member.getMemberRole()))
                return member.getNickname();
        }
        return optional.get();
    }
}
