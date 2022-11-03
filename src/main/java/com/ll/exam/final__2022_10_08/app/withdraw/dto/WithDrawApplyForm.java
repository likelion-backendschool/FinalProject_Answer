package com.ll.exam.final__2022_10_08.app.withdraw.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WithDrawApplyForm {
    @NotBlank
    private String bankName;
    @NotBlank
    private String bankAccountNo;
    @NotNull
    private Integer price;
}
