package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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

    public boolean isDuplicateTime(TeamDateDto teamDateDto, List<Member> members) {
        for (int i = 0; i < members.size(); i++) {
            LocalDateTime startTime = members.get(i).getTeam().getPeriod().getStartTime();
            LocalDateTime endTime = members.get(i).getTeam().getPeriod().getEndTime();
            if (startTime.getMonthValue() == teamDateDto.getMonth())
                if (startTime.getDayOfMonth() == teamDateDto.getDay()) {
                    if (teamDateDto.getStartHour() <= startTime.getHour()) {
                        if (teamDateDto.getEndHour() > startTime.getHour())
                            return false;
                    }
                    else if (teamDateDto.getEndHour() >= endTime.getHour()) {
                        if (teamDateDto.getStartHour() < endTime.getHour())
                            return false;
                    }
                }
        }
        return true;
    }
}
