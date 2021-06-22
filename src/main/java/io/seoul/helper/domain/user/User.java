package io.seoul.helper.domain.user;

import lombok.Builder;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor @Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String nickname, String fullname, String email, String picture, Role role) {
        this.nickname = nickname;
        this.fullname = fullname;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User updateProfile(String nickname, String fullname, String picture) {
        this.nickname = nickname;
        this.fullname = fullname;
        this.picture = picture;
        return this;
    }
}
