package com.ll.exam.final__2022_10_08.app.attr.entity;

import com.ll.exam.final__2022_10_08.app.base.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(
        indexes = {
                @Index(name = "uniqueIndex1", columnList = "relTypeCode, relId, typeCode, type2Code", unique = true),
                @Index(name = "index1", columnList = "relTypeCode, typeCode, type2Code")
        }
)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Attr extends BaseEntity {
    private String relTypeCode;
    private long relId;
    private String typeCode;
    private String type2Code;
    private String value;
    private LocalDateTime expireDate;
}
