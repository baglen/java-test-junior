package com.java.test.junior.load.controller;

import com.java.test.junior.load.service.EntityLoader;
import com.java.test.junior.security.details.DefaultUserDetails;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 09.03.2023
 */
@RestController
@RequestMapping("/loading")
@RequiredArgsConstructor
@Validated
public class LoadController {

    private final EntityLoader entityLoader;

    @PostMapping("products")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Data from file saved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "File validation failed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "message": "Provided file not found",
                                                 "status": 400
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
                                                "path": "/loading/products"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Only admin user can load products from file",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "timestamp": "2023-03-09T19:50:32.748+00:00",
                                                "status": 403,
                                                "error": "Forbidden",
                                                "path": "/loading/products"
                                            }"""
                            )
                    )
            )
    })
    public void loadProducts(@RequestParam("path") String path, @AuthenticationPrincipal DefaultUserDetails userDetails) {
        entityLoader.load(path, userDetails.getUserId());
    }
}
