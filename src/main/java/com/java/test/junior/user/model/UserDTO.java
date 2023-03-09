package com.java.test.junior.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
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
public class UserDTO {
    @JsonProperty("first_name")
    @NotBlank(message = "First name may not be blank")
    private String firstName;
    @JsonProperty("last_name")
    @NotBlank(message = "Last name may not be blank")
    private String lastName;
    @NotBlank(message = "Username name may not be blank")
    private String username;
    @NotBlank(message = "Password name may not be blank")
    private String password;
}
