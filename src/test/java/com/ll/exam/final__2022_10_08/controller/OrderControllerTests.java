package com.ll.exam.final__2022_10_08.controller;

import com.ll.exam.final__2022_10_08.app.order.controller.OrderController;
import com.ll.exam.final__2022_10_08.app.order.service.OrderService;
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
public class OrderControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문 목록")
    @WithUserDetails("user2")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/order/list"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("showList"))
                .andExpect(content().string(containsString("주문 목록")));
    }

    @Test
    @DisplayName("주문 상세페이지")
    @WithUserDetails("user2")
    void t2() throws Exception {
        // GIVEN
        long id = 2;

        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/order/%d".formatted(id)))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("showDetail"))
                .andExpect(content().string(containsString("주문 상세내용")));
    }

    @Test
    @DisplayName("주문 취소")
    @WithUserDetails("user2")
    void t3() throws Exception {
        // GIVEN
        long id = 5;

        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/order/%d/cancel".formatted(id))
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("cancel"))
                .andExpect(redirectedUrlPattern("/order/%d?msg=**".formatted(id)));
    }

    @Test
    @DisplayName("주문 환불")
    @WithUserDetails("user2")
    void t4() throws Exception {
        // GIVEN
        long id = 2;

        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/order/%d/refund".formatted(id))
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(OrderController.class))
                .andExpect(handler().methodName("refund"))
                .andExpect(redirectedUrlPattern("/order/%d?msg=**".formatted(id)));
    }
}
