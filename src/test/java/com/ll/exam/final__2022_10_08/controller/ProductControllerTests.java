package com.ll.exam.final__2022_10_08.controller;

import com.ll.exam.final__2022_10_08.app.product.controller.ProductController;
import com.ll.exam.final__2022_10_08.app.product.service.ProductService;
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
public class ProductControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 폼")
    @WithUserDetails("user2")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/product/create"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("showCreate"))
                .andExpect(content().string(containsString("상품 등록")));
    }

    @Test
    @DisplayName("상품 등록")
    @WithUserDetails("user2")
    void t2() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/product/create")
                        .with(csrf())
                        .param("subject", "상품명")
                        .param("price", "18000")
                        .param("postKeywordId", "1")
                        .param("productTagContents", "#올해 가장 인기있는 #IT")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(redirectedUrlPattern("/product/**"));

        Long productId = Long.valueOf(resultActions.andReturn().getResponse().getRedirectedUrl().replace("/product/", "").split("\\?", 2)[0]);
        assertThat(productService.findById(productId).isPresent()).isTrue();
    }
}
