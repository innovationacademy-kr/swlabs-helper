package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.member.dto.MemberResponseDto;
import io.seoul.helper.controller.review.dto.*;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.review.ReviewStatus;
import io.seoul.helper.domain.review.Score;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.Role;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.team.TeamRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
                .filter(m -> m.getParticipation())
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

    @Transactional(readOnly = true)
    public ReviewResponseDto findReviewByMemberId(Long memberId) throws Exception {
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new Exception("Not Exist Member!"));
        Review review = reviewRepo.findReviewByTeamAndUser(member.getTeam(), member.getUser())
                .orElseThrow(() -> new Exception("Not Exist Review!"));
        Score score = review.getScore();
        return ReviewResponseDto.builder()
                .id(review.getId())
                .score(ScoreDto.builder()
                        .fun(score.getFun())
                        .nice(score.getNice())
                        .time(score.getTime())
                        .interested(score.getInterested())
                        .build())
                .description(review.getDescription())
                .status(review.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ReviewNeedSettleResponseDto> findReviewsNotSettle(SessionUser sessionUser, int limit) throws Exception {
        Optional.of(sessionUser)
                .filter(o -> o.getRole() == Role.ADMIN)
                .orElseThrow(() -> new Exception("Not valid User"));
        Pageable p = PageRequest.of(0, limit);
        List<Review> lists = reviewRepo.findReviewsByNotSettle(p);
        return lists.stream()
                .map(review -> {
                    User user = review.getUser();
                    Team team = review.getTeam();
                    Member member = memberRepo.findMemberByTeamAndUser(review.getTeam(), review.getUser())
                            .orElseThrow(() -> new RuntimeException("Not exist member"));
                    return ReviewNeedSettleResponseDto.builder()
                            .id(review.getId())
                            .score(ScoreDto.builder()
                                    .fun(review.getScore().getFun())
                                    .nice(review.getScore().getNice())
                                    .time(review.getScore().getTime())
                                    .interested(review.getScore().getInterested())
                                    .build())
                            .description(review.getDescription())
                            .status(review.getStatus())
                            .member(MemberResponseDto.builder()
                                    .id(member.getId())
                                    .userId(user.getId())
                                    .nickname(user.getNickname())
                                    .memberRole(member.getRole().toString())
                                    .picture(user.getPicture())
                                    .creator(member.getCreator())
                                    .build())
                            .team(new TeamResponseDto(team))
                            .updated(review.getUpdated())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewNeedSettleCountResponseDto getReviewNeedSettleCount() {
        Long count = reviewRepo.getReviewNeedSettleCount();
        return ReviewNeedSettleCountResponseDto.builder()
                .count(count)
                .build();
    }
}
