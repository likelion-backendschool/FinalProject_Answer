package com.ll.exam.final__2022_10_08.service;

import com.ll.exam.final__2022_10_08.app.attr.service.AttrService;
import com.ll.exam.final__2022_10_08.util.Ut;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AttrServiceTests {
    @Autowired
    private AttrService attrService;

    @Test
    @DisplayName("영속변수 저장 및 조회")
    @Rollback(false)
    public void t1() {
        attrService.set("member__1__extra__homeTown", "전주");

        String member1HomeTown = attrService.get("member__1__extra__homeTown", null);
        assertThat(member1HomeTown).isEqualTo("전주");
    }

    @Test
    @DisplayName("영속변수, 숫자")
    public void t2() {
        attrService.set("member__1__extra__postsCount", 50);
        long member1CommonPostsCount = attrService.getAsLong("member__1__extra__postsCount", 0);

        assertThat(member1CommonPostsCount).isEqualTo(50);
    }

    @Test
    @DisplayName("영속변수, boolean")
    public void t3() {
        attrService.set("member__1__extra__married", true);
        boolean member1CommonMarried = attrService.getAsBoolean("member__1__extra__married", false);

        assertThat(member1CommonMarried).isEqualTo(true);
    }

    @Test
    @DisplayName("만료기간")
    public void t4() {
        attrService.set("member__1__extra__homeTown", "전주", LocalDateTime.now().plusSeconds(1));

        Ut.sleep(1000);

        String member1HomeTown = attrService.get("member__1__extra__homeTown", null);
        assertThat(member1HomeTown).isNull();
    }
}
