package com.java.test.junior.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ExceptionModel {
    private String message;
    private Integer status;
}
