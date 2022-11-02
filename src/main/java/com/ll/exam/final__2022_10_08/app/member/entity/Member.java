package com.ll.exam.final__2022_10_08.app.member.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.exam.final__2022_10_08.app.base.entity.BaseEntity;
import com.ll.exam.final__2022_10_08.app.member.entity.emum.AuthLevel;
import com.ll.exam.final__2022_10_08.util.Ut;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private boolean emailVerified;
    private long restCash;
    private String nickname;

    @Convert(converter = AuthLevel.Converter.class)
    private AuthLevel authLevel;

    @Column(columnDefinition = "TEXT")
    private String accessToken;

    public static Member fromMap(Map<String, Object> map) {
        return fromJwtClaims(map);
    }

    public static Member fromJwtClaims(Map<String, Object> jwtClaims) {
        long id = 0;

        if (jwtClaims.get("id") instanceof Long) {
            id = (long) jwtClaims.get("id");
        } else if (jwtClaims.get("id") instanceof Integer) {
            id = (long) (int) jwtClaims.get("id");
        }

        LocalDateTime createDate = null;
        LocalDateTime modifyDate = null;


        if (jwtClaims.get("createDate") instanceof LocalDateTime) {
            createDate = (LocalDateTime) jwtClaims.get("createDate");
        }
        else if (jwtClaims.get("createDate") instanceof List) {
            createDate = Ut.date.bitsToLocalDateTime((List<Integer>) jwtClaims.get("createDate"));
        }

        if (jwtClaims.get("modifyDate") instanceof LocalDateTime) {
            modifyDate = (LocalDateTime) jwtClaims.get("modifyDate");
        }
        else if (jwtClaims.get("modifyDate") instanceof List) {
            modifyDate = Ut.date.bitsToLocalDateTime((List<Integer>) jwtClaims.get("modifyDate"));
        }

        String username = (String) jwtClaims.get("username");
        String email = (String) jwtClaims.get("email");
        boolean emailVerified = (boolean) jwtClaims.get("emailVerified");
        AuthLevel authLevel = (AuthLevel) jwtClaims.get("authLevel");
        String accessToken = (String) jwtClaims.get("accessToken");
        String nickname = (String) jwtClaims.get("nickname");

        return Member
                .builder()
                .id(id)
                .createDate(createDate)
                .modifyDate(modifyDate)
                .username(username)
                .email(email)
                .emailVerified(emailVerified)
                .authLevel(authLevel)
                .accessToken(accessToken)
                .nickname(nickname)
                .build();
    }

    public String getName() {
        if (nickname != null) {
            return nickname;
        }

        return username;
    }

    public Member(long id) {
        super(id);
    }

    public String getJdenticon() {
        return "member__" + getId();
    }

    public List<GrantedAuthority> genAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        if (getAuthLevel() == AuthLevel.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }

        // 닉네임을 가지고 있다면 작가의 권한을 가진다.
        if (StringUtils.hasText(nickname)) {
            authorities.add(new SimpleGrantedAuthority("AUTHOR"));
        }

        return authorities;
    }

    @JsonIgnore
    public Map<String, Object> getAccessTokenClaims() {
        return Ut.mapOf(
                "id", getId(),
                "createDate", getCreateDate(),
                "modifyDate", getModifyDate(),
                "username", getUsername(),
                "email", getEmail(),
                "emailVerified", isEmailVerified(),
                "nickname", getNickname(),
                "authLevel", getAuthLevel(),
                "authorities", genAuthorities()
        );
    }

    @JsonIgnore
    public Map<String, Object> toMap() {
        return Ut.mapOf(
                "id", getId(),
                "createDate", getCreateDate(),
                "modifyDate", getModifyDate(),
                "username", getUsername(),
                "email", getEmail(),
                "emailVerified", isEmailVerified(),
                "nickname", getNickname(),
                "authLevel", getAuthLevel(),
                "authorities", genAuthorities(),
                "accessToken", getAccessToken()
        );
    }
}
