package io.seoul.helper.domain.user;

import io.seoul.helper.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_nickname", nullable = false)
    private String nickname;

    @Column(name = "user_fullname", nullable = false)
    private String fullname;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(name = "user_picture")
    private String picture;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Member> members = new ArrayList<>();

    @Builder
    public User(String nickname, String fullname, String email, String picture, Role role) {
        this.nickname = nickname;
        this.fullname = fullname;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String nickname, String fullname, String picture) {
        this.nickname = nickname;
        this.fullname = fullname;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
