package com.ll.exam.final__2022_10_08.app.myBook.controller;

import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.base.rq.Rq;
import com.ll.exam.final__2022_10_08.app.myBook.dto.*;
import com.ll.exam.final__2022_10_08.app.myBook.entity.MyBook;
import com.ll.exam.final__2022_10_08.app.myBook.service.MyBookService;
import com.ll.exam.final__2022_10_08.util.Ut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/myBooks", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1MyBooksController", description = "로그인 된 회윈이 구매한 책 정보")
public class ApiV1MyBooksController {
    private final MyBookService myBookService;
    private final Rq rq;

    @GetMapping(value = "", consumes = ALL_VALUE)
    @Operation(summary =  "로그인된 회원이 보유한 도서 목록", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RsData<MyBooksResponse>> myBooks() {
        List<MyBookDto> myBooks = myBookService
                .findAllByOwnerId(rq.getId())
                .stream()
                .map(MyBookDto::of).toList();

        return Ut.sp.responseEntityOf(
                RsData.successOf(
                        MyBooksResponse
                                .builder()
                                .myBooks(myBooks)
                                .build()
                )
        );
    }

    @GetMapping(value = "/{myBookId}", consumes = ALL_VALUE)
    @Operation(summary =  "로그인된 회원이 보유한 도서 단건", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RsData<MyBookResponse>> myBook(@PathVariable long myBookId) {
        MyBook myBook = myBookService.findByIdAndOwnerId(myBookId, rq.getId());
        List<BookChapterDto> bookChapters = myBookService.getBookChapters(myBook);

        return Ut.sp.responseEntityOf(
                RsData.successOf(
                        MyBookResponse.builder()
                                .myBook(MyBookDetailDto.of(myBook, bookChapters))
                                .build()
                )
        );
    }
}
