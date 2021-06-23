package io.seoul.helper.config.auth.dto;

import io.seoul.helper.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String fullname;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.nickname = user.getNickname();
        this.fullname = user.getFullname();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
