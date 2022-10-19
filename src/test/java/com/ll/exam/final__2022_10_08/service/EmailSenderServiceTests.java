package com.ll.exam.final__2022_10_08.service;

import com.ll.exam.final__2022_10_08.app.emailSender.service.EmailSenderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EmailSenderServiceTests {
    @Autowired
    private EmailSenderService emailSenderService;

    @Test
    @DisplayName("이메일 발송, 실제발송")
    public void t1() {
        // 주석의 이유 : 실제로 메일이 발송되기 때문에
        // 정말 필요할 때만 주석을 해제해서 테스트
        //emailSenderService.send("jangka512@gmail.com", "no-reply@no-reply.com", "[테스트발송] - 제목", "[테스트발송] - 내용");
    }
}
