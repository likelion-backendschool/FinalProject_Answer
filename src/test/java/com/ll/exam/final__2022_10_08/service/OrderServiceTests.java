package com.ll.exam.final__2022_10_08.service;


import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.cart.service.CartService;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.service.MemberService;
import com.ll.exam.final__2022_10_08.app.order.entity.Order;
import com.ll.exam.final__2022_10_08.app.order.service.OrderService;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import com.ll.exam.final__2022_10_08.app.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OrderServiceTests {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("주문")
    void t1() {
        // 상품 3, 상품 4 불러오기
        Product product3 = productService.findById(3).get();
        Product product4 = productService.findById(4).get();

        // 2번 회원 불러오기
        Member member2 = memberService.findById(2).get();

        // 2번 회원의 장바구니에 상품 2개 추가
        cartService.addItem(member2, product3);
        cartService.addItem(member2, product4);

        // 2번회원의 장바구니에 있는 상품으로 주문 생성
        Order order = orderService.createFromCart(member2);

        assertThat(order).isNotNull();
    }

    @Test
    @DisplayName("주문취소")
    void t2() {
        Member member2 = memberService.findById(2).get();

        // 상품 3, 상품 4 불러오기
        Order order3 = orderService.findById(3).get();

        orderService.cancel(order3, member2);

        assertThat(order3.isCanceled()).isTrue();
    }

    @Test
    @DisplayName("주문환불")
    void t3() {
        Member member2 = memberService.findById(2).get();

        // 상품 3, 상품 4 불러오기
        Order order2 = orderService.findById(2).get();

        RsData refundRsdata = orderService.refund(order2, member2);

        assertThat(refundRsdata.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("결제한 지 10분이 지난 주문은 환불을 할 수 없다.")
    void t4() {
        Member member1 = memberService.findById(1).get();

        // 상품 3, 상품 4 불러오기
        Order order1 = orderService.findById(1).get();

        RsData refundRsdata = orderService.refund(order1, member1);

        assertThat(refundRsdata.isSuccess()).isFalse();
    }
}
