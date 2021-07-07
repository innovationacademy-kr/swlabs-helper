package io.seoul.helper.repository.user;

import io.seoul.helper.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByNickname(String nickname);

    Optional<User> findUserByEmail(String email);
}
