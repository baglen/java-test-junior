/*
 * Copyright (c) 2013-2022 Global Database Ltd, All rights reserved.
 */

package com.java.test.junior.product.service;

import com.java.test.junior.model.PageableModel;
import com.java.test.junior.product.exception.ProductNotFoundException;
import com.java.test.junior.product.exception.UserIsNotProductOwnerException;
import com.java.test.junior.product.mapper.ProductMapper;
import com.java.test.junior.product.model.Product;
import com.java.test.junior.product.model.ProductDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dumitru.beselea
 * @version java-test-junior
 * @apiNote 08.12.2022
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    /**
     * @param productDTO this product to be created
     * @param userId     id of the product owner
     * @return the product created from the database
     */
    @Override
    public Product createProduct(ProductDTO productDTO, Long userId) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setUserId(userId);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.insert(product);
        return product;
    }

    /**
     * @param productId  id of the product to be updated
     * @param userId     id of the product owner
     * @param productDTO this product to be updated
     * @return the product updated from the database
     */
    @Override
    public Product updateProduct(Long productId, Long userId, ProductDTO productDTO) {
        Product product = productMapper.findById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        if (!product.getUserId().equals(userId)) {
            throw new UserIsNotProductOwnerException();
        }
        product.setName(productDTO.getName());
        product.setDescription(product.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.update(product);
        return product;
    }

    /**
     * @param productId id of the product to be fetched
     * @return the product updated from the database
     */
    @Override
    public Product getProductById(Long productId) {
        Product product = productMapper.findById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        return product;
    }

    /**
     * @param productId id of the product to be deleted
     * @param userId    id of the product owner
     */
    @Override
    public void deleteProduct(Long productId, Long userId) {
        Product product = productMapper.findById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        if (!product.getUserId().equals(userId)) {
            throw new UserIsNotProductOwnerException();
        }
        productMapper.delete(productId);
    }

    /**
     * @param page     number of page
     * @param pageSize count of objects on page
     * @param search   products by name
     * @return the list of products with page and data counts
     */
    @Override
    public PageableModel<Product> getAll(Integer page, Integer pageSize, String search) {
        List<Product> products = productMapper.findAll(page - 1, pageSize, search);
        Integer dataCount = productMapper.count(search);
        Integer pageCount = getPageCount(dataCount, pageSize);
        return new PageableModel<>(pageCount, dataCount, products);
    }

    /**
     * @param productId id of the product to be rated
     * @param userId    id of user who rates the product
     * @param isLiked   product rate
     */
    @Override
    public void rateProduct(Long productId, Long userId, Boolean isLiked) {
        if (!productMapper.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productMapper.deleteRate(productId, userId);
        productMapper.rateProduct(productId, userId, isLiked);
    }

    private int getPageCount(int dataCount, int pageSize) {
        double pageCount = 0;
        if (dataCount != 0) {
            if (dataCount == 1)
                pageCount = dataCount;
            else if (pageSize != 1)
                pageCount = (double) dataCount / pageSize;
            else
                pageCount = pageSize;
        }
        return (int) Math.ceil(pageCount);
    }
}