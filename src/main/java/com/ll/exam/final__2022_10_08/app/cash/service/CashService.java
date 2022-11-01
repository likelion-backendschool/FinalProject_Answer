package com.ll.exam.final__2022_10_08.app.cash.service;

import com.ll.exam.final__2022_10_08.app.cash.entity.CashLog;
import com.ll.exam.final__2022_10_08.app.cash.repository.CashLogRepository;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashService {
    private final CashLogRepository cashLogRepository;

    public CashLog addCash(Member member, long price, String eventType) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        return cashLog;
    }
}
