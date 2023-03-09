package com.java.test.junior.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    @JsonIgnore
    private String password;
    private Role role;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
