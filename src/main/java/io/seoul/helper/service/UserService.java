package io.seoul.helper.service;

import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.user.dto.UserResponseDto;
import io.seoul.helper.domain.user.User;
import io.seoul.helper.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
@EnableJpaAuditing
public class UserService {
    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public UserResponseDto findUserBySession(SessionUser currentUser) throws Exception {
        if (currentUser == null) throw new Exception("not login");
        User user = userRepo.findUserByNickname(currentUser.getNickname())
                .orElseThrow(() -> new EntityNotFoundException("invalid user"));
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getFullname())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public UserResponseDto findUserById(Long id) throws Exception {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("invalid user"));
        return UserResponseDto.builder()
                .name(user.getFullname())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}
