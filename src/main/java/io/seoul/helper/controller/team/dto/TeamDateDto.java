package io.seoul.helper.controller.team.dto;

import lombok.Getter;

@Getter
public class TeamDateDto {
        int month;
        int day;
        int startHour;
        int startMinute;
        int endHour;
        int endMinus;

        public TeamDateDto(TeamCreateRequestDto request) {
            this.month = request.getStartTime().getMonthValue();
            this.day = request.getStartTime().getDayOfMonth();
            this.startHour = request.getStartTime().getHour();
            this.startMinute = request.getStartTime().getMinute();
            this.endHour = request.getEndTime().getHour();
            this.endMinus = request.getEndTime().getMinute();
        }
}
