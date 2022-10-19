package com.ll.exam.final__2022_10_08.controller;

import com.ll.exam.final__2022_10_08.app.member.controller.MemberController;
import com.ll.exam.final__2022_10_08.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MemberControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 폼")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/join"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showJoin"))
                .andExpect(content().string(containsString("회원가입")));
    }

    @Test
    @DisplayName("회원가입")
    void t2() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf())
                        .param("username", "user10")
                        .param("password", "1234")
                        .param("email", "user1@test.com")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(redirectedUrlPattern("/member/login?msg=**"));

        assertThat(memberService.findByUsername("user10").isPresent()).isTrue();
    }

    @Test
    @DisplayName("아이디찾기 폼")
    void t3() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/findUsername"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showFindUsername"))
                .andExpect(content().string(containsString("아이디 찾기")));
    }

    @Test
    @DisplayName("아이디찾기")
    void t4() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/findUsername")
                        .with(csrf())
                        .param("email", "user1@test.com")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findUsername"))
                .andExpect(redirectedUrlPattern("/member/login?**"));
    }

    @Test
    @DisplayName("비밀번호 찾기 폼")
    void t5() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/findPassword"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showFindPassword"))
                .andExpect(content().string(containsString("비밀번호 찾기")));
    }

    @Test
    @DisplayName("비밀번호 찾기")
    void t6() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/findPassword")
                        .with(csrf())
                        .param("email", "user1@test.com")
                        .param("username", "user1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findPassword"))
                .andExpect(redirectedUrlPattern("/member/login?**"));
    }


}
