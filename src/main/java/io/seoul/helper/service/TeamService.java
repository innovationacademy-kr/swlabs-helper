package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.team.dto.*;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.project.Project;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.project.ProjectRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeamService {
    private final UserRepository userRepo;
    private final TeamRepository teamRepo;
    private final MemberRepository memberRepo;
    private final ProjectRepository projectRepo;
    private final MailSenderService mailSenderService;

    @Transactional
    public TeamResponseDto createNewTeamWish(SessionUser currentUser, TeamCreateRequestDto requestDto) throws Exception {
        User user = findUser(currentUser);
        Project project = findProject(requestDto.getProjectId());
        checkTimeValid(requestDto.getStartTime(), requestDto.getEndTime());
        Team team = requestDto.toEntity(project);
        team = teamRepo.save(team);
        Member member = Member.builder()
                .team(team)
                .user(user)
                .role(MemberRole.MENTEE)
                .creator(true)
                .build();
        memberRepo.save(member);
        return new TeamResponseDto(team);
    }

    @Transactional
    public TeamResponseDto updateTeamByMentor(SessionUser currentUser, Long teamId, TeamUpdateRequestDto requestDto) throws Exception {
        User user = findUser(currentUser);
        Team team = findTeam(teamId);
        Project project = findProject(requestDto.getProjectId());
        if (memberRepo.findMemberByTeamAndUser(team, user).isPresent())
            throw new Exception("Not valid member");
        checkTimeValid(team.getStartTime(), team.getEndTime(), requestDto.getStartTime(), requestDto.getEndTime());
        team.updateTeam(requestDto.getStartTime(), requestDto.getEndTime(),
                requestDto.getMaxMemberCount(), requestDto.getLocation(), project);
        team = teamRepo.save(team);
        memberRepo.save(Member.builder()
                .team(team)
                .user(user)
                .role(MemberRole.MENTOR)
                .creator(false)
                .build());
        List<Member> members = team.getMembers();
        for (Member member : members) {
            if (member.getCreator())
                mailSenderService.sendMatchMail(member.getUser(), team);
        }
        return new TeamResponseDto(team);
    }

    @Transactional
    public List<TeamResponseDto> updateTeamsLessThanCurrentTime() throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Team> teams = teamRepo.findTeamsByStatusNotAndEndTimeLessThan(TeamStatus.END, currentTime);

        if (teams.isEmpty()) {
            throw new EntityNotFoundException("nothing to change teams");
        }
        for (Team team : teams) {
            team.updateTeamEnd();
        }
        teams = teamRepo.saveAll(teams);

        return teams.stream().map(team -> new TeamResponseDto(team))
                .collect(Collectors.toList());
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
                .creator(false)
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
        Pageable pageable = PageRequest.of(
                requestDto.getOffset(), requestDto.getLimit(), Sort.Direction.DESC, "id");

        if (requestDto.getNickname() != null) {
            List<Long> teamIds = findTeamIdsByNickname(requestDto.getNickname(), requestDto.isCreateor());

            teams = teamRepo.findTeamsByTeamIdIn(
                    requestDto.getStartTime(), requestDto.getEndTime(), requestDto.getStatus(),
                    requestDto.getLocation(), teamIds, pageable);
        } else if (requestDto.getExcludeNickname() != null) {
            List<Long> teamIds = findTeamIdsByNickname(requestDto.getExcludeNickname(), requestDto.isCreateor());

            teams = teamRepo.findTeamsByTeamIdNotIn(
                    requestDto.getStartTime(), requestDto.getEndTime(), requestDto.getStatus(),
                    requestDto.getLocation(), teamIds, pageable);
        } else {
            teams = teamRepo.findTeamsByQueryParameters(
                    requestDto.getStartTime(), requestDto.getEndTime(), requestDto.getStatus(),
                    requestDto.getLocation(), pageable);
        }
        return teams.map(team -> new TeamResponseDto(team));
    }

    private List<Long> findTeamIdsByNickname(String nickName, boolean isCreator) {
        User user = userRepo.findUserByNickname(nickName).get();
        List<Member> members;
        if (isCreator)
            members = memberRepo.findMembersByUserAndCreator(user, true);
        else
            members = memberRepo.findMembersByUser(user);

        return members.stream()
                .map(m -> m.getTeam().getId())
                .collect(Collectors.toList());
    }

    private User findUser(SessionUser currentUser) throws Exception {
        if (currentUser == null) throw new Exception("not login");
        return userRepo.findUserByNickname(currentUser.getNickname())
                .orElseThrow(() -> new EntityNotFoundException("invalid user"));
    }

    private User findUser(String nickname) throws Exception {
        if (nickname == null) throw new Exception("not login");
        return userRepo.findUserByNickname(nickname)
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

    private void checkTimeValid(LocalDateTime preStartTime, LocalDateTime preEndTime, LocalDateTime newStartTime, LocalDateTime newEndTime) throws Exception {
        if (newStartTime.isAfter(newEndTime) || newStartTime.isEqual(newEndTime))
            throw new IllegalArgumentException("Invalid Time");
        if (preStartTime.isAfter(newStartTime) || preEndTime.isBefore(newEndTime))
            throw new IllegalArgumentException("Out of valid time bound");
    }

    public List<TeamLocationDto> findAllLocation() {
        return Arrays.stream(TeamLocation.values()).map((o) -> {
            return TeamLocationDto.builder()
                    .id(o.getId())
                    .code(o.name())
                    .name(o.getName())
                    .build();
        }).collect(Collectors.toList());
    }
}
