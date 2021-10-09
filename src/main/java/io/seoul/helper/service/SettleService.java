package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.review.dto.ReviewResponseDto;
import io.seoul.helper.controller.review.dto.ScoreDto;
import io.seoul.helper.controller.settle.dto.SettleNeedPaidGroupByUserResponseDto;
import io.seoul.helper.controller.settle.dto.SettlePayRequestDto;
import io.seoul.helper.controller.settle.dto.SettlePostRequestDto;
import io.seoul.helper.controller.settle.dto.SettleResponseDto;
import io.seoul.helper.controller.user.dto.UserResponseDto;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.settle.Settle;
import io.seoul.helper.domain.settle.SettleStatus;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.Role;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.settle.SettleRepository;
import io.seoul.helper.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class SettleService {
    private SettleRepository settleRepo;
    private UserRepository userRepo;
    private ReviewRepository reviewRepo;
    private MemberRepository memberRepo;

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

    @Transactional
    public List<SettleNeedPaidGroupByUserResponseDto> getSettleNeedPaidGroupByUser() {
        Map<Long, SettleNeedPaidGroupByUserResponseDto> map = new HashMap<>();
        Set<SettleStatus> statusSet = new HashSet<>();
        statusSet.add(SettleStatus.PASS);
        List<Settle> settles = settleRepo.findSettlesByStatusInAndWalletPaid(statusSet, false);
        settles.stream().forEach(o -> {
            Review review = o.getReview();
            User user = review.getUser();
            Team team = review.getTeam();
            Member member = memberRepo.findMemberByTeamAndUser(team, user)
                    .orElseThrow(() -> new RuntimeException("매칭되는 유저 없음"));
            SettleNeedPaidGroupByUserResponseDto dto = map.getOrDefault(user.getId(),
                    SettleNeedPaidGroupByUserResponseDto.builder()
                            .user(UserResponseDto.builder()
                                    .id(user.getId())
                                    .email(user.getEmail())
                                    .nickname(user.getNickname())
                                    .name(user.getFullname())
                                    .picture(user.getPicture())
                                    .build())
                            .menteeSettles(new ArrayList<>())
                            .mentorSettles(new ArrayList<>())
                            .build());
            try {
                if (member.getRole() == MemberRole.MENTOR)
                    dto.getMentorSettles().add(this.getSettle(o.getId()));
                else {
                    dto.getMenteeSettles().add(this.getSettle(o.getId()));
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
            map.put(user.getId(), dto);
        });
        return new ArrayList<>(map.values());
    }

    @Transactional
    public void payWallet(SessionUser userSession, List<SettlePayRequestDto> dtos) throws Exception {
        Optional<SessionUser> admin = Optional.ofNullable(userSession);
        User user = admin.map(o -> userRepo.getById(o.getId()))
                .filter(o -> o.getRole() == Role.ADMIN)
                .orElseThrow(() -> new Exception("관리자가 아닙니다."));
        dtos.forEach(o -> {
            Settle settle = settleRepo.findById(o.getId())
                    .orElseThrow(() -> new RuntimeException("요청한 정산목록을 찾을 수 없습니다."));
            if (!settle.payWallet()) throw new RuntimeException("해당 정산은 이미 지급처리 되었습니다.");
            settleRepo.save(settle);
        });
    }
}
