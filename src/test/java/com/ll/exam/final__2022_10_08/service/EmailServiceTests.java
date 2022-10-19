package com.ll.exam.final__2022_10_08.service;

import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.email.service.EmailService;
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
class EmailServiceTests {
    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("이메일 발송")
    public void t1() {
        RsData<Long> sendEmailRs = emailService.sendEmail("user1@test.com", "제목1", "내용1");
        long sendEmailLogId = sendEmailRs.getData();

        assertThat(sendEmailLogId).isGreaterThan(0);
    }

    @Test
    @DisplayName("이메일 2개 발송")
    public void t2() {
        RsData<Long> sendEmailRs1 = emailService.sendEmail("user1@test.com", "제목1", "내용1");
        RsData<Long> sendEmailRs2 = emailService.sendEmail("user1@test.com", "제목2", "내용2");

        assertThat(sendEmailRs2.getData()).isGreaterThan(sendEmailRs1.getData());
    }
}
