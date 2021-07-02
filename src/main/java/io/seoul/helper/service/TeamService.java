package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.team.dto.TeamCreateRequestDto;
import io.seoul.helper.controller.team.dto.TeamListRequestDto;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.project.Project;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.project.ProjectRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final UserRepository userRepo;
    private final TeamRepository teamRepo;
    private final MemberRepository memberRepo;
    private final ProjectRepository projectRepo;

    public TeamService(UserRepository userRepo, TeamRepository teamRepo, MemberRepository memberRepo,
                       ProjectRepository projectRepo) {
        this.userRepo = userRepo;
        this.teamRepo = teamRepo;
        this.memberRepo = memberRepo;
        this.projectRepo = projectRepo;
    }

    @Transactional
    public Long createNewTeamWish(SessionUser currentUser, TeamCreateRequestDto requestDto) throws Exception {
        if (currentUser == null) new Exception("not login");
        User user = userRepo.findUserByNickname(currentUser.getNickname())
                .orElseThrow(() -> new Exception("invalid user"));
        Project project = projectRepo.findProjectByName(requestDto.getProjectName()).
                orElseThrow(() -> new Exception("invalid project"));
        Team team = requestDto.toEntity(project);
        team = teamRepo.save(team);
        Member member = Member.builder()
                .team(team)
                .user(user)
                .role(MemberRole.MENTEE)
                .build();
        memberRepo.save(member);
        return team.getId();
    }

    public void createNewTeam() {

    }

    public void joinTeam() {

    }

    public void getJoinableTeamList() {

    }

    public void getJoinedTeamList() {

    }

    public void getMyTeamList() {

    }

    public void getTeamInfo() {

    }

    @Transactional
    public List<TeamResponseDto> findTeams(TeamListRequestDto requestDto) {
        List<Team> teams = teamRepo.findTeamsByQueryParameters(
                requestDto.getStartTime(), requestDto.getEndTime(), requestDto.getStatus(), requestDto.getLocation(),
                PageRequest.of(requestDto.getOffset(), requestDto.getLimit()));
        return teams.stream()
                .map(team -> new TeamResponseDto(team))
                .collect(Collectors.toList());
    }
}
