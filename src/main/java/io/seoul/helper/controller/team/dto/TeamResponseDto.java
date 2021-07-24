package io.seoul.helper.controller.team.dto;

import io.seoul.helper.controller.member.dto.MemberResponseDto;
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

    private String subject;
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<MemberResponseDto> members;

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
                .map(member -> MemberResponseDto.builder()
                        .id(member.getId())
                        .userId(member.getUser().getId())
                        .nickname(member.getUser().getNickname())
                        .memberRole(member.getRole().getName())
                        .creator(member.getCreator())
                        .build())
                .collect(Collectors.toList());
        this.subject = team.getSubject();
        this.description = team.getDescription();
    }

    public String getNicknameByRole(MemberRole memberRole) {
        String nickname = "empty";
        Optional<String> optional = Optional.of(nickname);

        for (MemberResponseDto member : this.members) {
            if (memberRole.getName().equals(member.getMemberRole()))
                return member.getNickname();
        }
        return optional.get();
    }

    public String getNicknameByMentee() {
        MemberRole memberRole = MemberRole.MENTEE;
        Optional<String> optional = Optional.of("empty");

        for (MemberResponseDto member : this.members) {
            if (memberRole.getName().equals(member.getMemberRole()))
                return member.getNickname();
        }
        return optional.get();
    }

    public String getNicknameByMentor() {
        MemberRole memberRole = MemberRole.MENTOR;
        Optional<String> optional = Optional.of("empty");

        for (MemberResponseDto member : this.members) {
            if (memberRole.getName().equals(member.getMemberRole()))
                return member.getNickname();
        }
        return optional.get();
    }

    public String getNicknameByCreator() {
        Optional<String> optional = Optional.of("empty");

        for (MemberResponseDto member : this.members) {
            if (member.getCreator())
                return member.getNickname();
        }
        return optional.get();
    }
}
