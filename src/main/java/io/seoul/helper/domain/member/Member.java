package io.seoul.helper.domain.member;

import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;

import javax.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;
}
