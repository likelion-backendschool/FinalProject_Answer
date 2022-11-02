package com.ll.exam.final__2022_10_08.app.product.dto;

import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ProductDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long authorId;
    private String authorName;
    private String subject;

    public static ProductDto of(Product product) {
        return ProductDto
                .builder()
                .id(product.getId())
                .createDate(product.getCreateDate())
                .modifyDate(product.getModifyDate())
                .authorId(product.getAuthor().getId())
                .authorName(product.getAuthor().getName())
                .subject(product.getSubject())
                .build();
    }
}
