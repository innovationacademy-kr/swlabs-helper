package io.seoul.helper.service;

import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.review.ReviewStatus;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamStatus;
import io.seoul.helper.repository.review.ReviewRepository;
import io.seoul.helper.repository.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class TeamInnerService {
    private final TeamRepository teamRepo;
    private final ReviewRepository reviewRepo;

    @Transactional
    public void endTeam(Long id) throws Exception {
        Team team = teamRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team is not exist"));
        List<Member> members = team.getMembers();

        if (team.getStatus() != TeamStatus.REVIEW)
            throw new Exception("This team cannot end");
        if (members.stream().anyMatch(m -> m.getParticipation() == null))
            return;
        if (members.stream()
                .filter(m -> m.getParticipation())
                .map(m -> reviewRepo.findReviewByTeamAndUser(m.getTeam(), m.getUser())
                        .orElseGet(() -> Review.builder()
                                .status(ReviewStatus.WAIT)
                                .build()))
                .anyMatch(r -> r.getStatus() == ReviewStatus.WAIT))
            return;
        team.updateTeamEnd();
        teamRepo.save(team);
    }
}
