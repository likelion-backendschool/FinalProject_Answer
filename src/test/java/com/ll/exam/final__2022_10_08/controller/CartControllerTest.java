package com.ll.exam.final__2022_10_08.controller;


import com.ll.exam.final__2022_10_08.app.cart.controller.CartController;
import com.ll.exam.final__2022_10_08.app.cart.service.CartService;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
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
public class CartControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CartService cartService;

    @Test
    @DisplayName("장바구니")
    @WithUserDetails("user1")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/cart/items"))
                .andDo(print());


        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(CartController.class))
                .andExpect(handler().methodName("showItems"))
                .andExpect(model().attributeExists("items"))
                .andExpect(content().string(containsString("장바구니")));
    }

    @Test
    @DisplayName("장바구니 품목추가")
    @WithUserDetails("user1")
    void t2() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/cart/addItem/1")
                                .with(csrf())
                )
                .andDo(print());

        System.out.println("cartService.getItemsByBuyer(new Member(1)) : " + cartService.getItemsByBuyer(new Member(1)));

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(CartController.class))
                .andExpect(handler().methodName("addItem"));

        boolean user1HasProduct1InCart = cartService.getItemsByBuyer(new Member(1))
                .stream()
                .anyMatch(cartItem -> cartItem.getProduct().getId().equals(1L));

        assertThat(user1HasProduct1InCart).isTrue();
    }

    @Test
    @DisplayName("장바구니 품목삭제")
    @WithUserDetails("user1")
    void t3() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/cart/removeItem/3")
                                .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(CartController.class))
                .andExpect(handler().methodName("removeItem"));

        boolean user1HasProduct3InCart = cartService.getItemsByBuyer(new Member(1))
                .stream()
                .anyMatch(cartItem -> cartItem.getProduct().getId().equals(3L));

        assertThat(user1HasProduct3InCart).isFalse();
    }
}
