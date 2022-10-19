package com.ll.exam.final__2022_10_08.controller;

import com.ll.exam.final__2022_10_08.app.post.controller.PostController;
import com.ll.exam.final__2022_10_08.app.post.entity.Post;
import com.ll.exam.final__2022_10_08.app.post.service.PostService;
import com.ll.exam.final__2022_10_08.app.postTag.entity.PostTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class PostControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PostService postService;

    @Test
    @DisplayName("글 작성 폼")
    @WithUserDetails("user1")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/post/write"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("showWrite"))
                .andExpect(content().string(containsString("글 작성")));
    }

    @Test
    @DisplayName("글 작성")
    @WithUserDetails("user1")
    void t2() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/post/write")
                        .with(csrf())
                        .param("subject", "제목1")
                        .param("content", "내용1")
                        .param("contentHtml", "내용1")
                        .param("postTagContents", "#IT")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("write"))
                .andExpect(redirectedUrlPattern("/post/**"));

        Long postId = Long.valueOf(resultActions.andReturn().getResponse().getRedirectedUrl().replace("/post/", "").split("\\?", 2)[0]);
        assertThat(postService.findById(postId).isPresent()).isTrue();
    }

    @Test
    @DisplayName("글 수정 폼")
    @WithUserDetails("user1")
    void t3() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/post/1/modify"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("showModify"))
                .andExpect(content().string(containsString("글 수정")));
    }

    @Test
    @DisplayName("글 수정")
    @WithUserDetails("user1")
    void t4() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/post/1/modify")
                                .with(csrf())
                                .param("subject", "제목1 NEW")
                                .param("content", "내용1 NEW")
                                .param("contentHtml", "내용1 NEW")
                                .param("postTagContents", "#IT #백엔드")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(redirectedUrlPattern("/post/**"));

        Long postId = Long.valueOf(resultActions.andReturn().getResponse().getRedirectedUrl().replace("/post/", "").split("\\?")[0]);

        Post post = postService.findById(postId).get();

        assertThat(post).isNotNull();
        assertThat(post.getSubject()).isEqualTo("제목1 NEW");
        assertThat(post.getContent()).isEqualTo("내용1 NEW");
        assertThat(post.getContentHtml()).isEqualTo("내용1 NEW");

        List<PostTag> postTags = postService.getPostTags(post);

        postTags.forEach(postTag ->
                assertThat(postTag.getPostKeyword().getContent()).containsAnyOf("IT", "백엔드"));
    }
}
