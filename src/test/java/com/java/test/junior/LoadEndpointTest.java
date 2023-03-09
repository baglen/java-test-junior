package com.java.test.junior;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.model.PageableModel;
import com.java.test.junior.product.model.Product;
import com.java.test.junior.product.model.ProductDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class LoadEndpointTest {

    private final String path = "/loading";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14"))
            .withDatabaseName("marketplace")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void postgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("DB_URL", postgreSQL::getJdbcUrl);
        registry.add("DB_USER", postgreSQL::getUsername);
        registry.add("DB_PASSWORD", postgreSQL::getPassword);
    }

    @SneakyThrows
    @Test
    @Transactional
    public void testLoadProducts(){
        this.mockMvc.perform(
                post(path + "/products")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                        .param("path", "src/test/resources/products.csv"))
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    @Transactional
    public void testLoadProductsFileNotFound(){
        this.mockMvc.perform(
                        post(path + "/products")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .param("path", "src/test/resources/products.csv1"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @Transactional
    public void testLoadProductsNotAuthorized(){
        this.mockMvc.perform(
                        post(path + "/products")
                                .param("path", "src/test/resources/products.csv"))
                .andExpect(status().isUnauthorized());
    }

    @SneakyThrows
    @Test
    @Transactional
    public void testLoadProductsMalformedData(){
        this.mockMvc.perform(
                        post(path + "/products")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .param("path", "src/test/resources/products-malformed.csv"))
                .andExpect(status().isBadRequest());
    }

}
