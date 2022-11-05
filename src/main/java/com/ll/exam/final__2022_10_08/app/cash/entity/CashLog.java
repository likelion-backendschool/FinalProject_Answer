package com.ll.exam.final__2022_10_08.app.cash.entity;


import com.ll.exam.final__2022_10_08.app.base.entity.BaseEntity;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class CashLog extends BaseEntity {
    private String relTypeCode;
    private Long relId;
    @ManyToOne(fetch = LAZY)
    private Member member;
    private long price; // 변동
    @Enumerated(EnumType.STRING)
    private EvenType eventType;

    public CashLog(long id) {
        super(id);
    }

    public enum EvenType {
        충전__무통장입금,
        충전__토스페이먼츠,
        출금__통장입금,
        사용__토스페이먼츠_주문결제,
        사용__예치금_주문결제,
        환불__예치금_주문결제,
        작가정산__예치금;
    }
}
