package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.review.dto.ReviewResponseDto;
import io.seoul.helper.controller.review.dto.ReviewUpdateRequestDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.review.ReviewStatus;
import io.seoul.helper.domain.review.Score;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {
    private final UserService userService;
    private final TeamInnerService teamInnerService;
    private final TeamRepository teamRepo;
    private final MemberRepository memberRepo;
    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;

    @Transactional
    public List<Long> createReviews(Long teamId) throws Exception {
        Team team = teamRepo.getById(teamId);
        List<Member> members = memberRepo.findMembersByTeam(team);

        List<Review> reviews = members.stream()
                .filter(m -> m.getParticipation() && m.getRole() == MemberRole.MENTEE)
                .map(m -> Review.builder()
                        .status(ReviewStatus.WAIT)
                        .team(team)
                        .user(m.getUser())
                        .score(Score.builder().fun(0).interested(0).nice(0).time(0).build())
                        .build())
                .collect(Collectors.toList());
        return reviewRepo.saveAll(reviews).stream()
                .map(Review::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateReview(SessionUser sessionUser, ReviewUpdateRequestDto requestDto) throws Exception {
        User user = userRepo.getById(userService.findUserBySession(sessionUser).getId());
        Review review = reviewRepo.findReviewByIdAndUser(requestDto.getId(), user)
                .orElseThrow(() -> new Exception("cannot find review"));
        switch (review.getStatus()) {
            case TIMEOUT:
                throw new Exception("This review is timeout");
            case UPDATED:
                throw new Exception("This review is already updated");
            default:
                break;
        }
        review.updateReview(requestDto.toEntitiy());
        reviewRepo.save(review);
        teamInnerService.endTeam(review.getTeam().getId());
    }

    public ReviewResponseDto getNewReview(SessionUser sessionUser, Long team_id) {
        try {
            User user = userRepo.getById(userService.findUserBySession(sessionUser).getId());
            Team team = teamRepo.getById(team_id);
            Review review = reviewRepo.findReviewByTeamAndUser(team, user)
                    .orElseThrow(() -> new Exception("Review is not created!"));
            if (review.getStatus() != ReviewStatus.WAIT)
                throw new Exception("Already updated review");
            return ReviewResponseDto.builder()
                    .id(review.getId())
                    .build();
        } catch (Exception e) {
            log.error("Unexpected access in getNewReview func" + e.getMessage());
            return ReviewResponseDto.builder()
                    .id(-1L)
                    .build();
        }
    }
}
