package com.ll.exam.final__2022_10_08.service;

import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberServiceTests {
    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입")
    void t1() {
        String username = "user10";
        String password = "1234";
        String email = "user10@test.com";

        memberService.join(username, password, email, null);

        Member foundMember = memberService.findByUsername("user10").get();
        assertThat(foundMember.getCreateDate()).isNotNull();
        assertThat(foundMember.getUsername()).isNotNull();
        assertThat(passwordEncoder.matches(password, foundMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원가입을 하면 일반회원이 된다.")
    void t2() {
        Member member = memberService.findById(2L).get();

        assertThat(member.getAuthLevel().getCode()).isEqualTo(3);
        assertThat(member.getAuthLevel().getValue()).isEqualTo("NORMAL");
    }
}
