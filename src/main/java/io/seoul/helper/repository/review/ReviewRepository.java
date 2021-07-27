package io.seoul.helper.repository.review;

import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findReviewByMember(Member member);
}
