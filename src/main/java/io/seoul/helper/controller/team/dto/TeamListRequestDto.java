package io.seoul.helper.controller.team.dto;

import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TeamListRequestDto {
    private int offset;
    private int limit;
    private String nickname;
    private String excludeNickname;
    private MemberRole memberRole;
    private boolean isCreateor;
    private List<TeamStatus> statusList;
    private TeamLocation location;
    private String sort;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTimePrevious;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTimePrevious;

    public TeamListRequestDto() {
        this.offset = 0;
        this.limit = 10;
        this.sort = "id,desc";
    }
}
