package io.seoul.helper.domain.member;

import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Member {
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

    @Builder
    public Member(Team team, User user, MemberRole role, Boolean creator) {
        this.team = team;
        this.user = user;
        this.role = role;
        this.creator = creator;
    }
}
