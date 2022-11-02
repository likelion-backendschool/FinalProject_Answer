package com.ll.exam.final__2022_10_08.app.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private boolean emailVerified;
    private String nickname;
    @JsonIgnore
    private String accessToken;

    public static MemberDto of(Member member) {
        return MemberDto
                .builder()
                .id(member.getId())
                .createDate(member.getCreateDate())
                .modifyDate(member.getModifyDate())
                .username(member.getUsername())
                .email(member.getEmail())
                .emailVerified(member.isEmailVerified())
                .nickname(member.getNickname())
                .build();
    }
}
