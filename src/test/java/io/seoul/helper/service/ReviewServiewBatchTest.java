package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.member.dto.MemberRequestDto;
import io.seoul.helper.controller.team.dto.TeamCreateRequestDto;
import io.seoul.helper.controller.team.dto.TeamUpdateRequestDto;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.review.ReviewStatus;
import io.seoul.helper.domain.review.Score;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.user.Role;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
public class ReviewServiewBatchTest {
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

    List<User> userList;
    Team team;
    List<Review> reviewList;

    @BeforeEach
    public void setup() {
        userList = new ArrayList<>();
        try {
            addUsers();
            createTeams();
            createReview();
        } catch (Exception e) {
            log.error(e.getMessage());
            fail("fail : cannot create team\nCause : " + e.getMessage());
        }
    }

    private void addUsers() {
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
        team = teamRepo.getById(teamService.createNewTeam(new SessionUser(userList.get(1)),
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

        teamService.updateTeamByMentor(new SessionUser(userList.get(0)),
                team.getId(),
                TeamUpdateRequestDto.builder()
                        .startTime(startTime)
                        .endTime(endTime)
                        .location(TeamLocation.ONLINE)
                        .maxMemberCount(4L)
                        .projectId(1L)
                        .build());

        memberService.joinTeam(new SessionUser(userList.get(2)),
                MemberRequestDto.builder()
                        .teamId(team.getId())
                        .role(MemberRole.MENTEE)
                        .build());

        memberService.joinTeam(new SessionUser(userList.get(3)),
                MemberRequestDto.builder()
                        .teamId(team.getId())
                        .role(MemberRole.MENTEE)
                        .build());
    }

    private void createReview() {
        reviewList = userList.stream()
                .map(u -> {
                    Review review = Review.builder()
                            .user(u)
                            .team(team)
                            .status(u.getId() % 2 == 0 ? ReviewStatus.WAIT : ReviewStatus.UPDATED)
                            .score(Score.builder().fun(4).nice(4).interested(4).time(4).build())
                            .description(String.format("BATCH TEST BY %s #%d", u.getNickname(), u.getId()))
                            .build();
                    return reviewRepo.save(review);
                }).collect(Collectors.toList());
    }

    @AfterEach
    public void cleanup() {
        reviewList.forEach(r -> {
            reviewRepo.findById(r.getId()).ifPresent(o -> reviewRepo.delete(o));
        });
        teamRepo.findById(team.getId()).ifPresent(o -> {
            memberRepo.findMembersByTeam(o).forEach(m -> memberRepo.delete(m));
            teamRepo.delete(o);
        });
        userList.forEach((u) -> {
            userRepo.findUserByNickname(u.getNickname()).ifPresent(o -> userRepo.delete(o));
        });
    }


    @Test
    @Transactional(readOnly = true)
    public void reviewTimeoutTestNotWork() {
        reviewService.updateReviewsTimeoutBatch(LocalDateTime.now().plusDays(6));
        reviewList.forEach(o -> assertNotEquals(o.getStatus(), ReviewStatus.TIMEOUT));
    }

    @Test
    @Transactional(readOnly = true)
    public void reviewTimeoutTest() {
        reviewService.updateReviewsTimeoutBatch(LocalDateTime.now().plusDays(7));
        reviewList.forEach(o -> checkReviewUpdated(reviewRepo.getById(o.getId())));
    }

    private void checkReviewUpdated(Review target) {
        Review review = Optional.ofNullable(target).orElseThrow(() -> new EntityNotFoundException("review not found"));
        assertNotEquals(review.getStatus(), ReviewStatus.WAIT);
        if (review.getStatus().equals(ReviewStatus.TIMEOUT)) {
            assertEquals(review.getUser().getId() % 2, 0);
        } else {
            assertEquals(review.getUser().getId() % 2, 1);
        }
        log.info(String.format("Review #%d : %s\nStatus = %s",
                review.getId(),
                review.getDescription(),
                review.getStatus().getName()));
    }
}
