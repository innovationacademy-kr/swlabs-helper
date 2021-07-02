package io.seoul.helper.repository.member;

import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findMembersByUser(User user);
}
