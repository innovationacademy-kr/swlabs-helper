package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.team.dto.TeamCreateRequestDto;
import io.seoul.helper.controller.team.dto.TeamListRequestDto;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.controller.team.dto.TeamUpdateRequestDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.project.Project;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.project.ProjectRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class TeamService {

    private final UserRepository userRepo;
    private final TeamRepository teamRepo;
    private final MemberRepository memberRepo;
    private final ProjectRepository projectRepo;

    public TeamService(UserRepository userRepo, TeamRepository teamRepo,
                       MemberRepository memberRepo,
                       ProjectRepository projectRepo) {
        this.userRepo = userRepo;
        this.teamRepo = teamRepo;
        this.memberRepo = memberRepo;
        this.projectRepo = projectRepo;
    }

    @Transactional
    public Long createNewTeamWish(SessionUser currentUser, TeamCreateRequestDto requestDto) throws Exception {
        User user = findUser(currentUser);
        Project project = findProject(requestDto.getProjectName());
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

    @Transactional
    public TeamResponseDto updateTeamByMentor(SessionUser currentUser, Long teamId, TeamUpdateRequestDto requestDto) throws Exception {
        User user = findUser(currentUser);
        Team team = findTeam(teamId);
        Project project = findProject(requestDto.getProjectName());
        Member member = memberRepo.findMemberByTeamAndUser(team, user)
                .orElseThrow(() -> new Exception("Not This Team Member"));

        if (member.getRole() != MemberRole.MENTEE || team.getStatus() != TeamStatus.WAITING)
            throw new Exception("This team cannot change");
        checkTimeValid(requestDto.getStartTime(), requestDto.getEndTime());
        team.updateTeam(requestDto.getStartTime(), requestDto.getEndTime(),
                requestDto.getMaxMemeberCount(), requestDto.getLocation(), project);
        teamRepo.save(team);

        return new TeamResponseDto(team);
    }

    @Transactional
    public void joinTeam(SessionUser sessionUser, Long id) throws Exception {
        User user = findUser(sessionUser);
        Team team = findTeam(id);
        if (memberRepo.findMemberByTeamAndUser(team, user).isPresent())
            throw new Exception("Already joined");
        if (team.getStatus() != TeamStatus.READY)
            throw new Exception("This Team is not ready");
        if (team.getCurrentMemberCount() >= team.getMaxMemberCount())
            throw new Exception("member is full");
        Member member = Member.builder()
                .team(team)
                .user(user)
                .role(MemberRole.MENTEE)
                .build();
        memberRepo.save(member);
    }

    @Transactional
    public void outTeam(SessionUser sessionUser, Long id) throws Exception {
        User user = findUser(sessionUser);
        Team team = findTeam(id);
        Member member = memberRepo.findMemberByTeamAndUser(team, user)
                .orElseThrow(() -> new Exception("Not this team member"));
        if (team.getStatus() != TeamStatus.READY)
            throw new Exception("This team is already running");
        memberRepo.delete(member);
    }

    @Transactional
    public void deleteTeam(SessionUser sessionUser, Long id) throws Exception {
        User user = findUser(sessionUser);
        Team team = findTeam(id);
        Member member = memberRepo.findMemberByTeamAndUser(team, user)
                .orElseThrow(() -> new Exception("Not this team member"));
        if (team.getStatus() != TeamStatus.WAITING)
            throw new Exception("This Team is already Matched!");
        memberRepo.delete(member);
        teamRepo.delete(team);
    }

    @Transactional
    public Page<TeamResponseDto> findTeams(TeamListRequestDto requestDto) {
        Page<Team> teams;
        String nickName = requestDto.getUserNickname();
        Pageable pageable = PageRequest.of(
                requestDto.getOffset(), requestDto.getLimit(), Sort.Direction.DESC, "id");

        if (nickName == null) {
            teams = teamRepo.findTeamsByQueryParameters(
                    requestDto.getStartTime(), requestDto.getEndTime(), requestDto.getStatus(),
                    requestDto.getLocation(), pageable);
        } else {
            teams = teamRepo.findTeamsByUserNickname(
                    requestDto.getStartTime(), requestDto.getEndTime(), requestDto.getStatus(),
                    requestDto.getLocation(), nickName, pageable);
        }

        return teams.map(team -> new TeamResponseDto(team));
    }

    private User findUser(SessionUser currentUser) throws Exception {
        if (currentUser == null) throw new Exception("not login");
        return userRepo.findUserByNickname(currentUser.getNickname())
                .orElseThrow(() -> new EntityNotFoundException("invalid user"));
    }

    private Team findTeam(Long teamId) throws EntityNotFoundException {
        return teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not exist!"));
    }

    private Project findProject(Long projectId) throws EntityNotFoundException {
        return projectRepo.findById(projectId).
                orElseThrow(() -> new EntityNotFoundException("invalid project"));
    }

    private Project findProject(String projectName) throws EntityNotFoundException {
        return projectRepo.findProjectByName(projectName).
                orElseThrow(() -> new EntityNotFoundException("invalid project"));
    }

    private void checkTimeValid(LocalDateTime startTime, LocalDateTime endTime) throws Exception {
        if (startTime != null && endTime != null &&
                (startTime.isAfter(endTime) || startTime.isEqual(endTime)))
            throw new IllegalArgumentException("Invalid Time");
    }
}
