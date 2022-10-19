package com.ll.exam.final__2022_10_08.app.product.repository;

import com.ll.exam.final__2022_10_08.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByIdDesc();
}
