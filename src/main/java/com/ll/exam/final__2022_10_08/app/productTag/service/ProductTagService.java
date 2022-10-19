package com.ll.exam.final__2022_10_08.app.productTag.service;

import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import com.ll.exam.final__2022_10_08.app.productKeyword.entity.ProductKeyword;
import com.ll.exam.final__2022_10_08.app.productKeyword.service.ProductKeywordService;
import com.ll.exam.final__2022_10_08.app.productTag.entity.ProductTag;
import com.ll.exam.final__2022_10_08.app.productTag.repository.ProductTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductTagService {
    private final ProductKeywordService productKeywordService;
    private final ProductTagRepository productTagRepository;

    @Transactional
    public void applyProductTags(Product product, String productTagContents) {
        List<ProductTag> oldProductTags = getProductTags(product);

        List<String> productKeywordContents = Arrays.stream(productTagContents.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<ProductTag> needToDelete = new ArrayList<>();

        for (ProductTag oldProductTag : oldProductTags) {
            boolean contains = productKeywordContents.stream().anyMatch(s -> s.equals(oldProductTag.getProductKeyword().getContent()));

            if (contains == false) {
                needToDelete.add(oldProductTag);
            }
        }

        needToDelete.forEach(productTag -> productTagRepository.delete(productTag));

        productKeywordContents.forEach(productKeywordContent -> {
            saveProductTag(product, productKeywordContent);
        });
    }

    private ProductTag saveProductTag(Product product, String productKeywordContent) {
        ProductKeyword productKeyword = productKeywordService.save(productKeywordContent);

        Optional<ProductTag> opProductTag = productTagRepository.findByProductIdAndProductKeywordId(product.getId(), productKeyword.getId());

        if (opProductTag.isPresent()) {
            return opProductTag.get();
        }

        ProductTag productTag = ProductTag.builder()
                .product(product)
                .member(product.getAuthor())
                .productKeyword(productKeyword)
                .build();

        productTagRepository.save(productTag);

        return productTag;
    }

    public List<ProductTag> getProductTags(Product product) {
        return productTagRepository.findAllByProductId(product.getId());
    }

    public List<ProductTag> getProductTagsByProductIdIn(long[] ids) {
        return productTagRepository.findAllByProductIdIn(ids);
    }

    public List<ProductTag> getProductTags(String productKeywordContent) {
        return productTagRepository.findAllByProductKeyword_contentOrderByProduct_idDesc(productKeywordContent);
    }
}
