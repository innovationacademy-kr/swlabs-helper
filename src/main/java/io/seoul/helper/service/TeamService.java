package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.team.dto.*;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.project.Project;
import io.seoul.helper.domain.team.Period;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.project.ProjectRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TeamService {
    private final UserRepository userRepo;
    private final TeamRepository teamRepo;
    private final MemberRepository memberRepo;
    private final ProjectRepository projectRepo;
    private final UserService userService;
    private final ReviewService reviewService;
    private final MemberService memberService;

    @Transactional
    public TeamResponseDto createNewTeam(SessionUser currentUser, TeamCreateRequestDto requestDto) throws Exception {
        User user = userRepo.getById(userService.findUserBySession(currentUser).getId());
        Project project = projectRepo.getById(requestDto.getProjectId());
        Period period = Period.builder()
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .build();
        List<Team> teamsByDuplicateDateTime = teamRepo.findTeamsByDuplicateDateTime(TeamStatus.REVOKE, TeamStatus.END, requestDto.getStartTime(), requestDto.getEndTime());
        log.info("team size : {}", teamsByDuplicateDateTime.size());
        teamsByDuplicateDateTime.stream().forEach(t -> {
            log.info("team status : {}",t.getStatus());
        });
        if (teamRepo.findTeamsByDuplicateDateTime(TeamStatus.REVOKE, TeamStatus.END,
                requestDto.getStartTime(), requestDto.getEndTime()).size() != 0) {
            throw new IllegalArgumentException("Time Overlap");
        }
        if (!period.isValid())
            throw new IllegalArgumentException("Invalid Time");
        if (requestDto.getMaxMemberCount() < 1 || requestDto.getMaxMemberCount() > 100)
            throw new IllegalArgumentException("Invalid people count");
        if (requestDto.getMemberRole() == null)
            throw new IllegalArgumentException("MemberRole is null.");

        Team team = requestDto.toEntity(project,
                requestDto.getMemberRole() == MemberRole.MENTEE ? TeamStatus.WAITING : TeamStatus.READY);
        team = teamRepo.save(team);
        Member member = Member.builder()
                .team(team)
                .user(user)
                .role(requestDto.getMemberRole())
                .creator(true)
                .build();
        memberRepo.save(member);
        return new TeamResponseDto(team);
    }

    @Transactional
    public TeamResponseDto updateTeamByMentor(SessionUser currentUser, Long teamId, TeamUpdateRequestDto requestDto) throws Exception {
        User user = userRepo.getById(userService.findUserBySession(currentUser).getId());
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team is not exist"));
        Project project = projectRepo.getById(requestDto.getProjectId());
        if (memberRepo.findMemberByTeamAndUser(team, user).isPresent())
            throw new Exception("Not valid member");
        Period period = Period.builder()
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .build();
        if (!period.isValid() || !team.getPeriod().isInRanged(period))
            throw new IllegalArgumentException("Invalid Time");
        team.updateTeam(period, requestDto.getMaxMemberCount(), requestDto.getLocation(), project);
        team.joinTeam();
        team = teamRepo.save(team);
        memberRepo.save(Member.builder()
                .team(team)
                .user(user)
                .role(MemberRole.MENTOR)
                .creator(false)
                .build());
        List<Member> members = team.getMembers();
        return new TeamResponseDto(team);
    }

    @Transactional
    public List<TeamResponseDto> updateTeamsLessThanCurrentTime() throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Team> teams = teamRepo.findTeamsByStatusNotAndEndTimeLessThan(TeamStatus.WAITING, currentTime);

        if (teams.isEmpty()) {
            throw new EntityNotFoundException("Nothing to change teams");
        }
        for (Team team : teams) {
            team.updateTeamTimeout();
        }
        teams = teamRepo.saveAll(teams);

        return teams.stream().map(TeamResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void revokeTeam(SessionUser currentUser, Long id) throws Exception {
        User user = userRepo.getById(userService.findUserBySession(currentUser).getId());
        Team team = teamRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team is not exist"));
        List<Member> members = memberRepo.findMembersByTeam(team);
        Member owner = members.stream()
                .filter(m -> m.getRole() == MemberRole.MENTOR).findAny()
                .orElseGet(() -> members.stream().filter(Member::getCreator).findAny()
                        .orElse(null));

        if (owner == null)
            throw new Exception("Team is not valid!");
        if (owner.getUser().getId() != user.getId())
            throw new Exception("Invalid User");

        members.forEach(m -> {
            m.updateParticipation(false);
            memberRepo.save(m);
        });
        team.updateTeamRevoke();
        teamRepo.save(team);
    }

    @Transactional
    public void reviewTeam(SessionUser currentUser, TeamReviewRequestDto requestDto) throws Exception {
        User user = userRepo.getById(currentUser.getId());
        Team team = teamRepo.findById(requestDto.getId())
                .orElseThrow(() -> new Exception("Team is not exist"));
        memberRepo.findMemberByTeamAndUserAndRole(team, user, MemberRole.MENTOR)
                .orElseThrow(() -> new Exception("Not this team mentor"));
        if (team.getStatus() != TeamStatus.READY && team.getStatus() != TeamStatus.FULL) {
            throw new Exception("Not progressed team");
        } else {
            team.updateTeamReview();
            teamRepo.save(team);
        }
        memberService.participateMembers(currentUser, requestDto);
        reviewService.createReviews(requestDto.getId());
    }

    private Pageable toPageable(int offset, int limit, String sort) throws Exception {
        Pageable pageable;
        try {
            if (sort.contains(",")) {
                String[] sortOption = sort.split(",");
                pageable = PageRequest.of(
                        offset, limit,
                        sortOption[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        sortOption[0]);
            } else {
                pageable = PageRequest.of(
                        offset, limit, Sort.Direction.DESC, sort);
            }
        } catch (Exception e) {
            throw new Exception("failed to Pageable");
        }
        return pageable;
    }

    @Transactional
    public Page<TeamResponseDto> findTeams(TeamListRequestDto requestDto) {
        Page<Team> teams;
        try {
            Pageable pageable = toPageable(requestDto.getOffset(), requestDto.getLimit(), requestDto.getSort());

            if (requestDto.getNickname() != null) {
                List<Long> teamIds = findTeamIdsByNickname(requestDto.getNickname(), requestDto.isCreateor(), requestDto.getMemberRole());

                teams = teamRepo.findTeamsByTeamIdIn(
                        requestDto.getStartTimePrevious(), requestDto.getEndTimePrevious(), requestDto.getStatusList(),
                        requestDto.getLocation(), teamIds, pageable);
            } else if (requestDto.getExcludeNickname() != null) {
                List<Long> teamIds = findTeamIdsByNickname(requestDto.getExcludeNickname(), requestDto.isCreateor(), requestDto.getMemberRole());

                if (teamIds.isEmpty()) {
                    teams = teamRepo.findTeamsByQueryParameters(
                            requestDto.getStartTimePrevious(), requestDto.getEndTimePrevious(), requestDto.getStatusList(),
                            requestDto.getLocation(), pageable);
                } else {
                    teams = teamRepo.findTeamsByTeamIdNotIn(
                            requestDto.getStartTimePrevious(), requestDto.getEndTimePrevious(), requestDto.getStatusList(),
                            requestDto.getLocation(), teamIds, pageable);
                }

            } else {
                teams = teamRepo.findTeamsByQueryParameters(
                        requestDto.getStartTimePrevious(), requestDto.getEndTimePrevious(), requestDto.getStatusList(),
                        requestDto.getLocation(), pageable);
            }
        } catch (Exception e) {
            log.error("failed to find teams : " + e.getMessage() + "\n\n" + e.getCause());
            return null;
        }
        return teams.map(TeamResponseDto::new);
    }

    private List<Long> findTeamIdsByNickname(String nickName, boolean isCreator, MemberRole memberRole) {
        User user = userRepo.findUserByNickname(nickName).
                orElseThrow(() -> new EntityNotFoundException("Invalid User"));
        List<Member> members;

        if (isCreator)
            members = memberRepo.findMembersByUserAndCreatorAndRole(user, true, memberRole);
        else
            members = memberRepo.findMembersByUserAndRole(user, memberRole);


        return members.stream()
                .map(m -> m.getTeam().getId())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TeamLocationDto> findAllLocation() {
        return Arrays.stream(TeamLocation.values()).map(o -> TeamLocationDto.builder()
                .id(o.getId())
                .code(o.name())
                .name(o.getName())
                .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public TeamResponseDto findTeam(Long teamId) throws EntityNotFoundException {
        return new TeamResponseDto(teamRepo.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team is not exist")));
    }


}
