package io.seoul.helper.domain.review;

import io.seoul.helper.domain.common.BaseTime;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseTime {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @LastModifiedBy
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String description;

    @Column
    private Score score;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ReviewStatus status;

    @Builder
    public Review(Team team, User user, Member member, String description, Score score, ReviewStatus status) {
        this.team = team;
        this.user = user;
        this.member = member;
        this.description = description;
        this.score = score;
        this.status = status;
    }
}
