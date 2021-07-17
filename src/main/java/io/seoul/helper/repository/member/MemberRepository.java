package io.seoul.helper.repository.member;

import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.member.MemberRole;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByTeamAndUser(Team team, User user);

    @Query("SELECT m FROM Member m " +
            "WHERE (:user is null or m.user = :user) and " +
            "(:isCreator is null or m.creator = :isCreator) and " +
            "(:memberRole is null or m.role = :memberRole)")
    List<Member> findMembersByUserAndCreatorAndRole(User user, boolean isCreator, MemberRole memberRole);
}
