package com.ll.exam.final__2022_10_08.app.cash.entity;


import com.ll.exam.final__2022_10_08.app.base.entity.BaseEntity;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
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
    private String eventType;

    public CashLog(long id) {
        super(id);
    }
}
