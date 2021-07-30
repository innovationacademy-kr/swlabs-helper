package io.seoul.helper.repository.review;

import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findReviewByTeamAndUser(Team team, User user);

    Optional<Review> findReviewByIdAndUser(Long id, User user);
}
