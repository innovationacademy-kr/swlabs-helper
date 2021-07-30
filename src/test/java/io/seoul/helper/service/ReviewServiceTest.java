package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.member.dto.MemberRequestDto;
import io.seoul.helper.controller.review.dto.ReviewUpdateRequestDto;
import io.seoul.helper.controller.team.dto.TeamCreateRequestDto;
import io.seoul.helper.controller.team.dto.TeamReviewRequestDto;
import io.seoul.helper.controller.team.dto.TeamUpdateRequestDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.domain.user.Role;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.yaml")
public class ReviewServiceTest {
    @Autowired
    private ReviewRepository reviewRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MemberRepository memberRepo;
    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private TeamService teamService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ReviewService reviewService;

    ArrayList<User> userList;
    ArrayList<Long> teamIdList;

    @BeforeAll
    public void setup() {
        try {
            addUsers();
            createTeams();
        } catch (Exception e) {
            fail("fail : cannot create team");
        }
    }

    private void addUsers() {
        userList = new ArrayList<>();
        userList.add(User.builder()
                .nickname("help_tester_1")
                .fullname("Hyunjin Won")
                .email("tester1@mail.com")
                .picture("")
                .role(Role.USER)
                .build());

        userList.add(User.builder()
                .nickname("help_tester_2")
                .fullname("Yungho Choi")
                .email("tester2@mail.com")
                .picture("")
                .role(Role.USER)
                .build()
        );
        userList.add(User.builder()
                .nickname("help_tester_3")
                .fullname("Mincheol Kang")
                .email("tester3@mail.com")
                .picture("")
                .role(Role.USER)
                .build()
        );
        userList.add(User.builder()
                .nickname("help_tester_4")
                .fullname("Gwangnam Heo")
                .email("tester4@mail.com")
                .picture("")
                .role(Role.ADMIN)
                .build()
        );
        for (User user : userList) {
            userRepo.save(user);
        }
    }

    private void createTeams() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0);
        teamIdList = new ArrayList<>();
        teamIdList.add(teamService.createNewTeam(new SessionUser(userList.get(1)),
                TeamCreateRequestDto.builder()
                        .subject("TEST TEAM MENTEE BUILD")
                        .description("TEST TEAM MENTEE BUILD")
                        .startTime(startTime)
                        .endTime(endTime)
                        .location(TeamLocation.ONLINE)
                        .maxMemberCount(4L)
                        .memberRole(MemberRole.MENTEE)
                        .projectId(1L)
                        .build()).getTeamId());

        teamIdList.add(teamService.createNewTeam(new SessionUser(userList.get(0)),
                TeamCreateRequestDto.builder()
                        .subject("TEST TEAM MENTOR BUILD")
                        .description("TEST TEAM MENTOR BUILD")
                        .startTime(startTime)
                        .endTime(endTime)
                        .location(TeamLocation.ONLINE)
                        .maxMemberCount(4L)
                        .memberRole(MemberRole.MENTOR)
                        .projectId(1L)
                        .build()).getTeamId());

        teamService.updateTeamByMentor(new SessionUser(userList.get(0)),
                teamIdList.get(0),
                TeamUpdateRequestDto.builder()
                        .startTime(startTime)
                        .endTime(endTime)
                        .location(TeamLocation.ONLINE)
                        .maxMemberCount(4L)
                        .projectId(1L)
                        .build());

        memberService.joinTeam(new SessionUser(userList.get(2)),
                MemberRequestDto.builder()
                        .teamId(teamIdList.get(0))
                        .role(MemberRole.MENTEE)
                        .build());

        memberService.joinTeam(new SessionUser(userList.get(3)),
                MemberRequestDto.builder()
                        .teamId(teamIdList.get(0))
                        .role(MemberRole.MENTEE)
                        .build());

        memberService.joinTeam(new SessionUser(userList.get(1)),
                MemberRequestDto.builder()
                        .teamId(teamIdList.get(1))
                        .role(MemberRole.MENTEE)
                        .build());

        memberService.joinTeam(new SessionUser(userList.get(2)),
                MemberRequestDto.builder()
                        .teamId(teamIdList.get(1))
                        .role(MemberRole.MENTEE)
                        .build());

        memberService.joinTeam(new SessionUser(userList.get(3)),
                MemberRequestDto.builder()
                        .teamId(teamIdList.get(1))
                        .role(MemberRole.MENTEE)
                        .build());
    }

    @AfterAll
    public void cleanup() {
        userList.stream().forEach((u) -> {
            Optional<User> target = userRepo.findUserByNickname(u.getNickname());
            target.ifPresent(o -> userRepo.delete(o));
        });
    }

    @Test
    public void reviewTest() throws Exception {
        checkReview(userList.get(0), TeamReviewRequestDto.builder()
                .id(teamIdList.get(0))
                .members(getMembersParticipation(teamIdList.get(0)))
                .build());
        checkReview(userList.get(0), TeamReviewRequestDto.builder()
                .id(teamIdList.get(1))
                .members(getMembersParticipation(teamIdList.get(1)))
                .build());
    }

    private List<TeamReviewRequestDto.MemberParticipation> getMembersParticipation(Long teamId) {
        List<TeamReviewRequestDto.MemberParticipation> list = new ArrayList<>();
        return memberRepo.findMembersByTeam(teamRepo.getById(teamId)).stream()
                .filter(m -> m.getRole() == MemberRole.MENTEE)
                .map(m -> TeamReviewRequestDto.MemberParticipation.builder()
                        .id(m.getId())
                        .participation(true)
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void checkReview(User user, TeamReviewRequestDto requestDto) throws Exception {
        teamService.reviewTeam(new SessionUser(user), requestDto);
        Team team = teamRepo.getById(requestDto.getId());
        List<User> users = memberRepo.findMembersByTeamAndAndRole(team, MemberRole.MENTEE).stream()
                .map(Member::getUser)
                .collect(Collectors.toList());
        users.forEach(m -> checkReviewCreated(reviewRepo.findReviewByTeamAndUser(team, m)));

        users.forEach(m -> {
            Review review = reviewRepo.findReviewByTeamAndUser(team, m)
                    .orElseGet(() -> {
                        fail("fail : cannot found review");
                        return new Review();
                    });
            ReviewUpdateRequestDto reviewUpdateRequestDto;
            reviewUpdateRequestDto = ReviewUpdateRequestDto.builder()
                    .id(review.getId())
                    .score(ReviewUpdateRequestDto.NewScore.builder()
                            .fun(4).interested(4).nice(4).time(4)
                            .build())
                    .description("ok")
                    .build();
            try {
                reviewService.updateReview(new SessionUser(m), reviewUpdateRequestDto);
            } catch (Exception e) {
                fail("fail : fail to update Review cuz " + e.getMessage());
            }
            checkReviewUpdated(reviewRepo.findById(reviewUpdateRequestDto.getId()));
        });
        Team chkTeam = teamRepo.findById(requestDto.getId()).orElseThrow(() -> new Exception());
        List<Member> members = chkTeam.getMembers();
        assertEquals(chkTeam.getStatus(), TeamStatus.END);
    }

    private void checkReviewCreated(Optional<Review> target) {
        Review review = target.orElseThrow(() -> new EntityNotFoundException("review not found"));
        assertNotNull(review.getUpdated());
        assertNotNull(review.getCreated());
    }

    private void checkReviewUpdated(Optional<Review> target) {
        Review review = target.orElseThrow(() -> new EntityNotFoundException("review not found"));
        assertNotNull(review.getUpdated());
        assertNotNull(review.getScore());
        assertNotNull(review.getDescription());
        assertNotEquals(review.getDescription(), "");
    }
}
