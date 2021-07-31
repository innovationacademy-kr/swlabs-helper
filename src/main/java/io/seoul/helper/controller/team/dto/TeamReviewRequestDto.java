package io.seoul.helper.controller.team.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamReviewRequestDto {
    private Long id;
    private List<MemberParticipation> members;

    @Builder
    public TeamReviewRequestDto(Long id, List<MemberParticipation> members) {
        this.id = id;
        this.members = members;
    }

    @Getter
    public static class MemberParticipation {
        private Long id;
        private Boolean participation;

        @Builder
        public MemberParticipation(Long id, Boolean participation) {
            this.id = id;
            this.participation = participation;
        }
    }
}
