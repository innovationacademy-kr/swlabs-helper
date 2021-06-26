package io.seoul.helper.domain.member;

import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "member_user_id", nullable = false)
    private User user;

    @Column(name = "member_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;
}
