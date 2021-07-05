package io.seoul.helper.repository.member;

import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByTeamAndUser(Team team, User user);

    List<Member> findMembersByUser(User user);

}
