package com.ll.exam.final__2022_10_08.app.myBook.dto;

import com.ll.exam.final__2022_10_08.app.myBook.entity.MyBook;
import com.ll.exam.final__2022_10_08.app.product.dto.ProductDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class MyBookDetailDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long ownerId;
    private ProductDetailDto product;

    public static MyBookDetailDto of(MyBook myBook, List<BookChapterDto> bookChapters) {
        return MyBookDetailDto
                .builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .modifyDate(myBook.getModifyDate())
                .ownerId(myBook.getOwner().getId())
                .product(ProductDetailDto.of(myBook.getProduct(), bookChapters))
                .build();
    }
}
