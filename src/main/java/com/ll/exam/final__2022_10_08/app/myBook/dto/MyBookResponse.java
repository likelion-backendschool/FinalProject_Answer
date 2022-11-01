package com.ll.exam.final__2022_10_08.app.myBook.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyBookResponse {
    private MyBookDetailDto myBook;
}
