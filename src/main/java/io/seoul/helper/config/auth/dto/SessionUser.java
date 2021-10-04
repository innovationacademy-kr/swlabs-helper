package io.seoul.helper.config.auth.dto;

import io.seoul.helper.domain.user.Role;
import io.seoul.helper.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private Long id;
    private String nickname;
    private String fullname;
    private String email;
    private String picture;
    private Role role;

    public SessionUser(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.fullname = user.getFullname();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRole();
    }
}
