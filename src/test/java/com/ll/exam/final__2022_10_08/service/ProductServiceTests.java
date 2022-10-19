package com.ll.exam.final__2022_10_08.service;

import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.repository.MemberRepository;
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
public class ProductServiceTests {
    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("상품 등록")
    void t1() {
        Member author = memberRepository.findByUsername("user1").get();

        Product product3 = productService.create(author, "상품명3", 50_000, "자바", "#IT #자바");
        Product product4 = productService.create(author, "상품명4", 60_000, "프론트엔드", "#IT #프론트엔드");

        assertThat(product3).isNotNull();
        assertThat(product4).isNotNull();
    }

    @Test
    @DisplayName("상품 수정")
    void t2() {
        Product product = productService.findById(2).get();

        productService.modify(product, "상품명2 NEW", 70_000, "#기술 #테크");

        assertThat(product.getSubject()).isEqualTo("상품명2 NEW");
        assertThat(product.getPrice()).isEqualTo(70_000);
    }
}
