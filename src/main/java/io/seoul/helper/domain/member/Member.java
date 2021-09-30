package io.seoul.helper.domain.member;

import io.seoul.helper.domain.common.BaseTime;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Member extends BaseTime {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    private Boolean creator;

    @Column(nullable = false)
    private Boolean participation;

    public void updateParticipation(Boolean check) {
        this.participation = check;
    }

    @Builder
    public Member(Team team, User user, MemberRole role, Boolean creator) {
        this.team = team;
        this.user = user;
        this.role = role;
        this.creator = creator;
        this.participation = true;
    }
}
