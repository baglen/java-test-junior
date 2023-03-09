/*
 * Copyright (c) 2013-2022 Global Database Ltd, All rights reserved.
 */

package com.java.test.junior.product.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dumitru.beselea
 * @version java-test-junior
 * @apiNote 08.12.2022
 */
@Getter
@Setter
public class ProductDTO {
    @NotBlank(message = "Product name may not be blank")
    private String name;
    @NotNull(message = "Product price may not be null")
    private Double price;
    @Size(max = 255, message = "Product description may not be greater than 255 symbols")
    private String description;
}