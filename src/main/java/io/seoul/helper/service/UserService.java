package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;

    @Transactional
    public User findUserBySession(SessionUser currentUser) throws Exception {
        if (currentUser == null) throw new Exception("not login");
        return userRepo.findUserByNickname(currentUser.getNickname())
                .orElseThrow(() -> new EntityNotFoundException("invalid user"));
    }

    @Transactional
    public User findUserById(Long id) throws Exception {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("invalid user"));
    }
}
