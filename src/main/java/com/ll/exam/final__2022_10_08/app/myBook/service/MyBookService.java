package com.ll.exam.final__2022_10_08.app.myBook.service;

import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.myBook.entity.MyBook;
import com.ll.exam.final__2022_10_08.app.myBook.repository.MyBookRepository;
import com.ll.exam.final__2022_10_08.app.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final MyBookRepository myBookRepository;

    @Transactional
    public RsData add(Order order) {
        order.getOrderItems().forEach(orderItem -> {
            MyBook book = MyBook.builder()
                    .owner(order.getBuyer())
                    .orderItem(orderItem)
                    .product(orderItem.getProduct())
                    .build();
            myBookRepository.save(book);
        });

        return RsData.of("S-1", "나의 책장에 추가되었습니다.");
    }
}
