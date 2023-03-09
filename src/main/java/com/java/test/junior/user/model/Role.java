package com.java.test.junior.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Role {
    private Long id;
    private String name;
}
