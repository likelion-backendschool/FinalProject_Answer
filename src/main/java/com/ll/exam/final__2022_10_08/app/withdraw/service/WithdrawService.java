package com.ll.exam.final__2022_10_08.app.withdraw.service;

import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.cash.entity.CashLog;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import com.ll.exam.final__2022_10_08.app.member.service.MemberService;
import com.ll.exam.final__2022_10_08.app.withdraw.entity.WithdrawApply;
import com.ll.exam.final__2022_10_08.app.withdraw.repository.WithdrawApplyRepository;
import com.ll.exam.final__2022_10_08.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class WithdrawService {
    private final WithdrawApplyRepository withdrawApplyRepository;
    private final MemberService memberService;

    public RsData<WithdrawApply> apply(String bankName, String bankAccountNo, Integer price, Member applicant) {
        WithdrawApply withdrawApply = WithdrawApply.builder()
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .price(price)
                .applicant(applicant)
                .build();

        withdrawApplyRepository.save(withdrawApply);

        return RsData.of("S-1", "출금 신청이 완료되었습니다.", withdrawApply);
    }

    public List<WithdrawApply> findAll() {
        return withdrawApplyRepository.findAll();
    }

    public RsData withdraw(Long withdrawApplyId) {
        WithdrawApply withdrawApply = withdrawApplyRepository.findById(withdrawApplyId).orElse(null);

        if (withdrawApply == null) {
            return RsData.of("F-1", "출금신청 데이터를 찾을 수 없습니다.");
        }

        if (withdrawApply.isApplyDoneAvailable() == false) {
            return RsData.of("F-2", "이미 처리되었습니다.");
        }

        CashLog cashLog = memberService.addCash(
                withdrawApply.getApplicant(),
                withdrawApply.getPrice() * -1,
                "출금__%d__지급__현금".formatted(withdrawApply.getId())
        ).getData().getCashLog();

        withdrawApply.setApplyDone(cashLog.getId());

        return RsData.of(
                "S-1",
                "출금신청(%d번) 처리완료. %s원이 출금되었습니다.".formatted(withdrawApply.getId(), Ut.nf(withdrawApply.getPrice())),
                Ut.mapOf(
                        "cashLogId", cashLog.getId()
                )
        );
    }
}
