package com.ll.exam.final__2022_10_08.app.productTag.repository;

import com.ll.exam.final__2022_10_08.app.productTag.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
    Optional<ProductTag> findByProductIdAndProductKeywordId(Long productId, Long keywordId);

    List<ProductTag> findAllByProductId(long productId);

    List<ProductTag> findAllByProductIdIn(long[] ids);

    List<ProductTag> findAllByProductKeyword_contentOrderByProduct_idDesc(String productKeywordContent);
}
