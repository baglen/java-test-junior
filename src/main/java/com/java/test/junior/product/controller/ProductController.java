/*
 * Copyright (c) 2013-2022 Global Database Ltd, All rights reserved.
 */

package com.java.test.junior.product.controller;

import com.java.test.junior.exception.ExceptionModel;
import com.java.test.junior.model.PageableModel;
import com.java.test.junior.product.model.Product;
import com.java.test.junior.product.model.ProductDTO;
import com.java.test.junior.product.service.ProductService;
import com.java.test.junior.security.details.DefaultUserDetails;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dumitru.beselea
 * @version java-test-junior
 * @apiNote 08.12.2022
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    implementation = Product.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Product body validation failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "timestamp": "2023-03-09T07:25:31.496+00:00",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "path": "/products"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed, provide auth data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "timestamp": "2023-03-09T07:28:25.372+00:00",
                                                "status": 401,
                                                "error": "Unauthorized",
                                                "path": "/products"
                                            }"""
                            )
                    )
            )
    })
    public Product createProduct(@Valid @NotNull @RequestBody ProductDTO productDTO,
                                 @AuthenticationPrincipal DefaultUserDetails userDetails) {
        return productService.createProduct(productDTO, userDetails.getUserId());
    }

    @GetMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product has been successfully fetched",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Product.class,
                                    type = "object"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ExceptionModel.class,
                                    type = "object"
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "Product with id: 20 not found",
                                                "status": 404
                                            }"""
                            )
                    )
            )
    })
    public Product getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Product body validation failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "timestamp": "2023-03-09T07:25:31.496+00:00",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "path": "/products"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "timestamp": "2023-03-09T07:28:25.372+00:00",
                                                "status": 401,
                                                "error": "Unauthorized",
                                                "path": "/products"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ExceptionModel.class,
                                    type = "object"
                            )
                    )
            )
    })
    public Product updateProduct(@PathVariable("id") Long id,
                                 @Valid @NotNull @RequestBody ProductDTO productDTO,
                                 @AuthenticationPrincipal DefaultUserDetails userDetails) {
        return productService.updateProduct(id, userDetails.getUserId(), productDTO);
    }

    @DeleteMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product has been successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "timestamp": "2023-03-09T07:28:25.372+00:00",
                                                "status": 401,
                                                "error": "Unauthorized",
                                                "path": "/products"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ExceptionModel.class,
                                    type = "object"
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "Product with id: 20 not found",
                                                "status": 404
                                            }"""
                            )
                    )
            )
    })
    public void deleteProduct(@PathVariable("id") Long id,
                              @AuthenticationPrincipal DefaultUserDetails userDetails) {
        productService.deleteProduct(id, userDetails.getUserId());
    }

    @GetMapping
    public PageableModel<Product> findAll(@RequestParam("page") Integer page,
                                          @RequestParam("pageSize") Integer pageSize,
                                          @RequestParam(name = "search", required = false) String search) {
        return productService.getAll(page, pageSize, search);
    }

    @GetMapping("{id}/like")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product has been successfully liked"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "timestamp": "2023-03-09T07:28:25.372+00:00",
                                                "status": 401,
                                                "error": "Unauthorized",
                                                "path": "/products"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ExceptionModel.class,
                                    type = "object"
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "Product with id: 20 not found",
                                                "status": 404
                                            }"""
                            )
                    )
            )
    })
    public void likeProduct(@PathVariable("id") Long id,
                            @AuthenticationPrincipal DefaultUserDetails userDetails) {
        productService.rateProduct(id, userDetails.getUserId(), true);
    }

    @GetMapping("{id}/dislike")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product has been successfully disliked"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "timestamp": "2023-03-09T07:28:25.372+00:00",
                                                "status": 401,
                                                "error": "Unauthorized",
                                                "path": "/products"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested product not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ExceptionModel.class,
                                    type = "object"
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "message": "Product with id: 20 not found",
                                                "status": 404
                                            }"""
                            )
                    )
            )
    })
    public void dislikeProduct(@PathVariable("id") Long id,
                               @AuthenticationPrincipal DefaultUserDetails userDetails) {
        productService.rateProduct(id, userDetails.getUserId(), false);
    }
}