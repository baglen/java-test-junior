/*
 * Copyright (c) 2013-2022 Global Database Ltd, All rights reserved.
 */

package com.java.test.junior.product.mapper;

import com.java.test.junior.product.model.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author dumitru.beselea
 * @version java-test-junior
 * @apiNote 08.12.2022
 */
@Mapper
public interface ProductMapper {
    Product findById(Long id);

    void insert(Product product);

    void update(Product product);

    void delete(Long id);

    Integer count(String search);

    List<Product> findAll(Integer page, Integer pageSize, String search);

    void rateProduct(Long productId, Long userId, Boolean isLiked);

    void deleteRate(Long productId, Long userId);

    Boolean existsById(Long id);
}