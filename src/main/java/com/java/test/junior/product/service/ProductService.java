/*
 * Copyright (c) 2013-2022 Global Database Ltd, All rights reserved.
 */

package com.java.test.junior.product.service;

import com.java.test.junior.model.PageableModel;
import com.java.test.junior.product.model.Product;
import com.java.test.junior.product.model.ProductDTO;

import java.util.List;

/**
 * @author dumitru.beselea
 * @version java-test-junior
 * @apiNote 08.12.2022
 */
public interface ProductService {
    /**
     * @param productDTO this product to be created
     * @param userId     id of the product owner
     * @return the product created from the database
     */
    Product createProduct(ProductDTO productDTO, Long userId);

    /**
     * @param productId id of the product to be updated
     * @param userId    id of the product owner
     * @return the product updated from the database
     */
    Product updateProduct(Long productId, Long userId, ProductDTO productDTO);

    /**
     * @param productId id of the product to be fetched
     * @return the product updated from the database
     */
    Product getProductById(Long productId);

    /**
     * @param productId id of the product to be deleted
     * @param userId    id of the product owner
     */
    void deleteProduct(Long productId, Long userId);

    /**
     * @param page     number of page
     * @param pageSize count of objects on page
     * @param search   products by name
     * @return the list of products with page and data counts
     */
    PageableModel<Product> getAll(Integer page, Integer pageSize, String search);

    /**
     * @param productId id of the product to be rated
     * @param userId    id of user who rates the product
     * @param isLiked   product rate
     */
    void rateProduct(Long productId, Long userId, Boolean isLiked);
}