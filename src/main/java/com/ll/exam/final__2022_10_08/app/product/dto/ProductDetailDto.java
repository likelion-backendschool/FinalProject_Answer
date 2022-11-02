package com.ll.exam.final__2022_10_08.app.product.dto;

import com.ll.exam.final__2022_10_08.app.myBook.dto.BookChapterDto;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ProductDetailDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long authorId;
    private String authorName;
    private String subject;
    private List<BookChapterDto> bookChapters;

    public static ProductDetailDto of(Product product, List<BookChapterDto> bookChapters) {
        return ProductDetailDto
                .builder()
                .id(product.getId())
                .createDate(product.getCreateDate())
                .modifyDate(product.getModifyDate())
                .authorId(product.getAuthor().getId())
                .authorName(product.getAuthor().getName())
                .subject(product.getSubject())
                .bookChapters(bookChapters)
                .build();
    }
}
