package com.ll.exam.final__2022_10_08.app.myBook.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MyBooksResponse {
    private List<MyBookDto> myBooks;
}
