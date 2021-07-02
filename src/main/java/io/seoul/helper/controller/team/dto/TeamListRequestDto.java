package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class TeamListRequestDto {

    private int offset;
    private int limit;
    private String userNickname;
    private MemberRole memberRole;
    private TeamStatus status;
    private TeamLocation location;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    public TeamListRequestDto() {
        this.offset = 0;
        this.limit = 10;
    }
}
