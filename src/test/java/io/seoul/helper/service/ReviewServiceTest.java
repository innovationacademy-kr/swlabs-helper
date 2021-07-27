package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.member.dto.MemberRequestDto;
import io.seoul.helper.controller.team.dto.TeamCreateRequestDto;
import io.seoul.helper.controller.team.dto.TeamUpdateRequestDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.user.Role;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@AllArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ReviewServiceTest {
    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;
    private final MemberRepository memberRepo;
    private final TeamRepository teamRepo;

    private final TeamService teamService;
    private final MemberService memberService;
    private final ReviewService reviewService;

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
        for (User user : userList) {
            Optional<User> target = userRepo.findUserByNickname(user.getNickname());
            target.ifPresent(o -> userRepo.delete(o));
        }
    }

    @Test
    public void reviewTest() {
        try {
            checkReview(userList.get(0), teamIdList.get(0));
            checkReview(userList.get(0), teamIdList.get(1));
        } catch (Exception e) {
            fail("fail : cannot create reviews");
        }
    }

    private void checkReview(User user, Long teamId) throws Exception {
        teamService.endTeam(new SessionUser(user), teamId);
        List<Long> reviewIds = reviewService.createReviews(teamId);
        Team team = teamRepo.getById(teamId);
        List<Member> members = memberRepo.findMembersByTeam(team);
        members.stream().forEach(m -> {
            checkReviewCreated(reviewRepo.findReviewByMember(m));
        });

        reviewIds.stream().forEach(i -> {
            reviewService.updateReview(new SessionUser(user), reviewIds);
            checkReviewUpdated(reviewRepo.findById(i));
        });
    }

    private void checkReviewCreated(Optional<Review> target) {
        Review review = target.orElseThrow(() -> {
            throw new EntityNotFoundException("review not found");
        });
        assertNull(review.getUpdated());
        assertNotNull(review.getCreated());
    }

    private void checkReviewUpdated(Optional<Review> target) {
        Review review = target.orElseThrow(() -> {
            throw new EntityNotFoundException("review not found");
        });
        assertNotNull(review.getUpdated());
        assertNotNull(review.getScore());
        assertNotNull(review.getDescription());
        assertNotEquals(review.getDescription(), "");
    }
}
