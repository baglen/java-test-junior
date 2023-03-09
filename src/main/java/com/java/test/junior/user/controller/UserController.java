package com.java.test.junior.user.controller;

import com.java.test.junior.user.model.User;
import com.java.test.junior.user.model.UserDTO;
import com.java.test.junior.user.service.UserService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    implementation = User.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User body validation failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "timestamp": "2023-03-09T07:25:31.496+00:00",
                                                "status": 400,
                                                "error": "Bad Request",
                                                "path": "/users"
                                            }"""
                            )
                    )
            )
    })
    public User registerUser(@Valid @NotNull @RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }
}
