package com.ll.exam.final__2022_10_08.app.myBook.dto;

import com.ll.exam.final__2022_10_08.app.myBook.entity.MyBook;
import com.ll.exam.final__2022_10_08.app.product.dto.ProductDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MyBookDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long ownerId;
    private ProductDto product;

    public static MyBookDto of(MyBook myBook) {
        return MyBookDto
                .builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .modifyDate(myBook.getModifyDate())
                .ownerId(myBook.getOwner().getId())
                .product(ProductDto.of(myBook.getProduct()))
                .build();
    }
}
