package io.seoul.helper.controller.review.dto;

import io.seoul.helper.controller.member.dto.MemberResponseDto;
import io.seoul.helper.controller.team.dto.TeamResponseDto;
import io.seoul.helper.domain.review.ReviewStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewNeedSettleResponseDto {
    private Long id;
    private String description;
    private ScoreDto score;
    private ReviewStatus status;
    private TeamResponseDto team;
    private MemberResponseDto member;
    private LocalDateTime updated;

    @Builder
    public ReviewNeedSettleResponseDto(Long id, String description, ScoreDto score, ReviewStatus status,
                                       TeamResponseDto team, MemberResponseDto member, LocalDateTime updated) {
        this.id = id;
        this.description = description;
        this.score = score;
        this.status = status;
        this.team = team;
        this.member = member;
        this.updated = updated;
    }
}