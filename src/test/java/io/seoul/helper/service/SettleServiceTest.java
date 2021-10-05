package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.review.dto.ReviewResponseDto;
import io.seoul.helper.controller.settle.dto.SettlePostRequestDto;
import io.seoul.helper.controller.settle.dto.SettleResponseDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.review.ReviewStatus;
import io.seoul.helper.domain.review.Score;
import io.seoul.helper.domain.settle.SettleStatus;
import io.seoul.helper.domain.team.Period;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.domain.user.Role;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.project.ProjectRepository;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.settle.SettleRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.yaml")
public class SettleServiceTest {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SettleService settleService;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private SettleRepository settleRepo;

    private List<Review> reviewList;
    private List<User> userList;
    private List<Team> teamList;
    private List<SettleResponseDto> settleList;

    @BeforeAll
    public void setup() {
        reviewList = new ArrayList<>();
        userList = new ArrayList<>();
        teamList = new ArrayList<>();
        settleList = new ArrayList<>();
        try {
            addUsers();
            createTeams();
            createReview();
        } catch (Exception e) {
            fail("fail : cannot create team");
        }
    }

    private void createReview() {
        userList.forEach(o -> {
            Review review = Review.builder()
                    .team(teamList.get(0))
                    .user(o)
                    .score(Score.builder()
                            .fun(4)
                            .interested(4)
                            .nice(4)
                            .time(4)
                            .build())
                    .status(o.getRole() == Role.ADMIN ? ReviewStatus.TIMEOUT : ReviewStatus.UPDATED)
                    .description("TEST REVIEW BY " + o.getNickname())
                    .build();
            review = reviewRepo.save(review);
            reviewList.add(review);
        });
    }

    private void addUsers() {
        userList.add(User.builder()
                .nickname("help_tester_admin")
                .fullname("Hyunjin Won")
                .email("tester1@mail.com")
                .picture("")
                .role(Role.ADMIN)
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
                .role(Role.USER)
                .build()
        );
        for (User user : userList) {
            userRepo.save(user);
        }
    }

    private void createTeams() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0);
        Team team = Team.builder()
                .subject("Settle Test Team")
                .description("Settle Test Team")
                .location(TeamLocation.ONLINE)
                .maxMemberCount(10L)
                .period(Period.builder().startTime(startTime).endTime(endTime).build())
                .status(TeamStatus.END)
                .project(projectRepo.getById(1L))
                .build();
        team = teamRepo.save(team);
        teamList.add(team);

        userList.forEach(o -> {
            Member member = Member.builder()
                    .team(teamList.get(0))
                    .user(o)
                    .creator(true)
                    .role(o.getRole() == Role.ADMIN ? MemberRole.MENTOR : MemberRole.MENTEE)
                    .build();
            memberRepo.save(member);
        });
    }

    @Test
    @Order(1)
    public void findTargetReview() {
        try {
            List<ReviewResponseDto> rst = reviewService.findReviewsNotSettle(new SessionUser(userList.get(0)), 10);
            if (rst.size() <= 0) {
                fail("fail to find target review");
            }
            rst.forEach(o -> log.info(o.getDescription()));
        } catch (Exception e) {
            fail("fail to find reviewService : " + e.getMessage());
        }
    }

    @Test
    public void findTargetLimit() {
        try {
            List<ReviewResponseDto> rst = reviewService.findReviewsNotSettle(new SessionUser(userList.get(0)), 3);
            if (rst.size() > 3)
                fail("fail to find reviewServiceLimit : overload limit");
        } catch (Exception e) {
            fail("fail to find reviewServiceLimit : " + e.getMessage());
        }
    }

    @Test
    public void findTargetByInvalidUser() {
        try {
            reviewService.findReviewsNotSettle(new SessionUser(userList.get(1)), 10);
            fail("fail to findReviewNotSettle : allow invalid user");
        } catch (Exception e) {
            log.info("PASS INVALID ROLE USER");
        }
        try {
            reviewService.findReviewsNotSettle(new SessionUser(userList.get(1)), 10);
            fail("fail to findReviewNotSettle : allow null user");
        } catch (Exception e) {
            log.info("PASS INVALID NULL USER");
        }
    }

    @Test
    @Order(2)
    public void settleReview() {
        SessionUser sessionUser = new SessionUser(userList.get(0));
        try {
            while (true) {
                List<ReviewResponseDto> reviews = reviewService.findReviewsNotSettle(sessionUser, 2);
                if (reviews.isEmpty()) break;
                reviews.forEach(o -> {
                    try {
                        SettleResponseDto dto = settleService.postSettle(sessionUser, SettlePostRequestDto.builder()
                                .reviewId(o.getId())
                                .status(SettleStatus.PASS)
                                .build());
                        settleList.add(dto);
                        log.info("REVIEW #" + o.getId() + " setteld by " + dto.getAdmin().getNickname() +
                                " : " + dto.getStatus());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            fail("fail to settleReview : " + e.getMessage());
        }
    }

    @AfterAll
    public void cleanup() {
        settleList.forEach(s -> {
            settleRepo.findById(s.getId()).ifPresent(o -> settleRepo.delete(o));
        });
        reviewList.forEach(r -> {
            reviewRepo.findById(r.getId()).ifPresent(o -> reviewRepo.delete(o));
        });
        teamList.forEach(t -> {
            teamRepo.findById(t.getId()).ifPresent(o -> {
                memberRepo.findMembersByTeam(o).forEach(m -> memberRepo.delete(m));
                teamRepo.delete(o);
            });
        });
        userList.forEach((u) -> {
            userRepo.findUserByNickname(u.getNickname()).ifPresent(o -> userRepo.delete(o));
        });
    }
}
