package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.member.dto.MemberRequestDto;
import io.seoul.helper.controller.team.dto.TeamReviewRequestDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepo;
    private final TeamRepository teamRepo;
    private final UserRepository userRepo;
    private final UserService userService;

    @Transactional
    public void joinTeam(SessionUser currentUser, MemberRequestDto requestDto) throws Exception {
        User user = userRepo.getById(userService.findUserBySession(currentUser).getId());
        Team team = teamRepo.findById(requestDto.getTeamId()).orElseThrow(() ->
                new EntityNotFoundException("Team not exist"));
        List<TeamStatus> statusList = new ArrayList<>();
        statusList.add(TeamStatus.REVOKE);
        statusList.add(TeamStatus.END);
        teamRepo.findTeamsByUserAndDuplicateDateTime(statusList, user.getId(),
                team.getPeriod().getStartTime(), team.getPeriod().getEndTime())
                .stream()
                .findAny()
                .ifPresent(o -> {
                    throw new RuntimeException("Time Overlap - Team #" + o.getId());
                });
        if (memberRepo.findMemberByTeamAndUser(team, user).isPresent())
            throw new Exception("Already joined");
        if (team.getStatus() != TeamStatus.READY)
            throw new Exception("This Team is not ready");
        if (team.getCurrentMemberCount() >= team.getMaxMemberCount())
            throw new Exception("member is full");
        else {
            team.joinTeam();
            teamRepo.save(team);
        }
        Member member = Member.builder()
                .team(team)
                .user(user)
                .creator(false)
                .role(requestDto.getRole())
                .build();
        memberRepo.save(member);
    }

    @Transactional
    public void outTeam(SessionUser currentUser, MemberRequestDto requestDto) throws Exception {
        User user = userRepo.getById(userService.findUserBySession(currentUser).getId());
        Team team = teamRepo.findById(requestDto.getTeamId()).orElseThrow(() ->
                new EntityNotFoundException("Team not exist"));
        Member member = memberRepo.findMemberByTeamAndUser(team, user)
                .orElseThrow(() -> new Exception("Not this team member"));
        if (team.getStatus() == TeamStatus.END)
            throw new Exception("This team is end status");
        if (team.getStatus() == TeamStatus.WAITING)
            throw new Exception("This team is waiting status");
        if (isCreator(member))
            throw new Exception("Creator can not leave the team");
        if (isMentor(member)) {
            throw new Exception("Mentor can not leave the team");
        }
        team.outTeam();
        teamRepo.save(team);
        memberRepo.delete(member);
    }

    private boolean isCreator(Member member) {
        return member.getCreator();
    }

    private boolean isMentor(Member member) {
        return member.getRole() == MemberRole.MENTOR;
    }

    @Transactional
    public void participateMembers(SessionUser currentUser, TeamReviewRequestDto requestDto) throws Exception {
        User user = userRepo.getById(userService.findUserBySession(currentUser).getId());
        Team team = teamRepo.getById(requestDto.getId());
        memberRepo.findMemberByTeamAndUserAndRole(team, user, MemberRole.MENTOR)
                .orElseThrow(() -> new Exception("Not this team mentor"));
        List<Member> mentees = new ArrayList<>();

        for (TeamReviewRequestDto.MemberParticipation m : requestDto.getMembers()) {
            Member mentee = memberRepo.findById(m.getId())
                    .orElseThrow(() -> new Exception("Not this team mentee"));
            mentee.updateParticipation(m.getParticipation());
            mentees.add(mentee);
        }
        memberRepo.saveAll(mentees);
    }
}
