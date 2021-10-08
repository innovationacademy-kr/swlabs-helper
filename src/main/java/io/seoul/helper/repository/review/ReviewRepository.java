package io.seoul.helper.repository.review;

import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findReviewByTeamAndUser(Team team, User user);

    Optional<Review> findReviewByIdAndUser(Long id, User user);

    @Query(value = "SELECT r " +
            "FROM Review r " +
            "LEFT JOIN Settle s " +
            "ON r.id = s.review.id " +
            "WHERE s.review.id is NULL AND r.status = 'UPDATED' ORDER BY r.updated DESC, r.id DESC"
    )
    List<Review> findReviewsByNotSettle(Pageable pageable);

    @Query(value = "SELECT count(r.id) " +
            "FROM Review r " +
            "LEFT JOIN Settle s " +
            "ON r.id = s.review.id " +
            "WHERE s.review.id is NULL AND r.status = 'UPDATED' " +
            "GROUP BY r.status"
    )
    Long getReviewNeedSettleCount();
}
