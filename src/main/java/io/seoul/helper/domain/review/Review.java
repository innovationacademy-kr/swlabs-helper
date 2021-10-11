package io.seoul.helper.domain.review;

import io.seoul.helper.domain.common.BaseTime;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseTime {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String description;

    @Embedded
    private Score score;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ReviewStatus status;

    @Builder
    public Review(Team team, User user, String description, Score score, ReviewStatus status) {
        this.team = team;
        this.user = user;
        this.description = description;
        this.score = score;
        this.status = status;
    }

    public Review updateReview(Review review) {
        this.description = review.description;
        this.score = review.score;
        this.status = ReviewStatus.UPDATED;
        return this;
    }

    public Review timeout() {
        if (status.equals(ReviewStatus.WAIT))
            this.status = ReviewStatus.TIMEOUT;
        return this;
    }
}
