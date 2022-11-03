package com.ll.exam.final__2022_10_08.app.withdraw.entity;

import com.ll.exam.final__2022_10_08.app.base.entity.BaseEntity;
import com.ll.exam.final__2022_10_08.app.cash.entity.CashLog;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class WithdrawApply extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member applicant;
    private String bankName;
    private String bankAccountNo;
    private int price;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private CashLog withdrawCashLog; // 출금에 관련된 내역
    private LocalDateTime withdrawDate;

    public WithdrawApply(long id) {
        super(id);
    }

    public boolean isApplyDoneAvailable() {
        if (withdrawDate != null || withdrawCashLog != null) {
            return false;
        }

        return true;
    }

    public void setApplyDone(Long cashLogId) {
        withdrawDate = LocalDateTime.now();
        this.withdrawCashLog = new CashLog(cashLogId);
    }
}