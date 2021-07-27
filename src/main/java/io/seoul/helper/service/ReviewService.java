package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.repository.member.MemberRepository;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.team.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {
    private final TeamRepository teamRepo;
    private final MemberRepository memberRepo;
    private final ReviewRepository reviewRepo;

    @Transactional
    public List<Long> createReviews(Long teamId) throws Exception {
        Team team = teamRepo.getById(teamId);
        List<Member> members = memberRepo.findMembersByTeam(team);

        return null;
    }

    @Transactional
    public void updateReview(SessionUser sessionUser, List<Long> reviewIds) {
    }
}
