package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.review.dto.ReviewResponseDto;
import io.seoul.helper.controller.review.dto.ScoreDto;
import io.seoul.helper.controller.settle.dto.SettlePostRequestDto;
import io.seoul.helper.controller.settle.dto.SettleResponseDto;
import io.seoul.helper.controller.user.dto.UserResponseDto;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.settle.Settle;
import io.seoul.helper.domain.user.Role;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.settle.SettleRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SettleService {
    private SettleRepository settleRepo;
    private UserRepository userRepo;
    private ReviewRepository reviewRepo;

    @Transactional
    public SettleResponseDto postSettle(SessionUser userSession, SettlePostRequestDto dto) throws Exception {
        Optional<SessionUser> admin = Optional.ofNullable(userSession);
        User user = admin.map(o -> userRepo.getById(o.getId()))
                .filter(o -> o.getRole() == Role.ADMIN)
                .orElseThrow(() -> new Exception("관리자가 아닙니다."));
        Review review = reviewRepo.getById(dto.getReviewId());
        Settle settle = Settle.builder()
                .admin(user)
                .review(review)
                .status(dto.getStatus())
                .walletPaid(false)
                .build();
        settleRepo.save(settle);

        return SettleResponseDto.builder()
                .id(settle.getId())
                .admin(UserResponseDto.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .build())
                .review(ReviewResponseDto.builder()
                        .id(review.getId())
                        .score(ScoreDto.builder()
                                .fun(review.getScore().getFun())
                                .nice(review.getScore().getNice())
                                .time(review.getScore().getTime())
                                .interested(review.getScore().getInterested())
                                .build())
                        .description(review.getDescription())
                        .build())
                .status(settle.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public SettleResponseDto getSettle(Long id) throws Exception {
        Settle settle = settleRepo.findById(id).orElseThrow(() -> new Exception("Cannot found resource"));
        User user = settle.getAdmin();
        Review review = settle.getReview();

        return SettleResponseDto.builder()
                .id(settle.getId())
                .admin(UserResponseDto.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .build())
                .review(ReviewResponseDto.builder()
                        .id(review.getId())
                        .score(ScoreDto.builder()
                                .fun(review.getScore().getFun())
                                .nice(review.getScore().getNice())
                                .time(review.getScore().getTime())
                                .interested(review.getScore().getInterested())
                                .build())
                        .description(review.getDescription())
                        .build())
                .status(settle.getStatus())
                .build();
    }
}
