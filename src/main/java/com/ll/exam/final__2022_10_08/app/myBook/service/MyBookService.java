package com.ll.exam.final__2022_10_08.app.myBook.service;

import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.myBook.dto.BookChapterDto;
import com.ll.exam.final__2022_10_08.app.myBook.entity.MyBook;
import com.ll.exam.final__2022_10_08.app.myBook.exception.MyBookNotFoundException;
import com.ll.exam.final__2022_10_08.app.myBook.repository.MyBookRepository;
import com.ll.exam.final__2022_10_08.app.order.entity.Order;
import com.ll.exam.final__2022_10_08.app.post.service.PostService;
import com.ll.exam.final__2022_10_08.app.postTag.entity.PostTag;
import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final MyBookRepository myBookRepository;
    private final PostService postService;

    @Transactional
    public RsData add(Order order) {
        order.getOrderItems()
                .stream()
                .map(orderItem -> MyBook.builder()
                        .owner(order.getBuyer())
                        .orderItem(orderItem)
                        .product(orderItem.getProduct())
                        .build())
                .forEach(myBookRepository::save);

        return RsData.of("S-1", "나의 책장에 추가되었습니다.");
    }

    @Transactional
    public RsData remove(Order order) {
        order.getOrderItems()
                .stream()
                .forEach(orderItem -> myBookRepository.deleteByProductIdAndOwnerId(orderItem.getProduct().getId(), order.getBuyer().getId()));

        return RsData.of("S-1", "나의 책장에서 제거되었습니다.");
    }

    public List<MyBook> findAllByOwnerId(long ownerId) {
        return myBookRepository.findAllByOwnerId(ownerId);
    }

    public MyBook findByIdAndOwnerId(long myBookId, long ownerId) {
        return myBookRepository.findByIdAndOwnerId(myBookId, ownerId).orElseThrow(MyBookNotFoundException::new);
    }

    public List<BookChapterDto> getBookChapters(MyBook myBook) {
        Product product = myBook.getProduct();

        List<PostTag> postTags = postService.getPostTags(product.getAuthor(), product.getPostKeyword().getContent());

        return postTags
                .stream()
                .map(postTag -> postTag.getPost())
                .map(post -> BookChapterDto.of(post))
                .collect(Collectors.toList());
    }
}
