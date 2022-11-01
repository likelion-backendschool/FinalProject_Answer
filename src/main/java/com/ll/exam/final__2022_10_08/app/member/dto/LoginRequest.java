package com.ll.exam.final__2022_10_08.app.member.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
    @NotEmpty(message = "username 을(를) 입력해주세요.")
    private String username;
    @NotEmpty(message = "password 을(를) 입력해주세요.")
    private String password;
}
