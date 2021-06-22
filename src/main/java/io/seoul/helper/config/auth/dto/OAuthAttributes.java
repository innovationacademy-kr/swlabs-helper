package io.seoul.helper.config.auth.dto;

import io.seoul.helper.domain.user.Role;
import io.seoul.helper.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String fullname;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname,
                           String fullname, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.fullname = fullname;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registerationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofIntra42(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofIntra42(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String)attributes.get("login"))
                .fullname((String)attributes.get("displayname"))
                .email((String)attributes.get("email"))
                .picture((String)attributes.get("image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .fullname(fullname)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}
