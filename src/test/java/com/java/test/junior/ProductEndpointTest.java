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
public class ProductEndpointTest {

    private final String path = "/products";

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
    public void createProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String result = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product product = objectMapper.readValue(result, Product.class);
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(product.getName(), productDTO.getName());
        Assertions.assertEquals(product.getDescription(), productDTO.getDescription());
        Assertions.assertEquals(product.getPrice(), productDTO.getPrice());
    }

    @SneakyThrows
    @Test
    @Transactional
    public void createProductNotValidBody() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void createProductNoAuthProvided() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:asd".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void getByIdExistingProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String resultExpected = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productExpected = objectMapper.readValue(resultExpected, Product.class);
        String resultActual = this.mockMvc.perform(
                        get(path + "/" + productExpected.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productActual = objectMapper.readValue(resultActual, Product.class);
        Assertions.assertEquals(productExpected, productActual);
    }

    @Test
    @SneakyThrows
    @Transactional
    public void getByIdNotExistingProduct() {
        this.mockMvc.perform(
                        get(path + "/" + 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updateProductTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String resultExpected = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productExpected = objectMapper.readValue(resultExpected, Product.class);
        productDTO.setPrice(120.0);
        String resultActual = this.mockMvc.perform(
                        put(path + "/" + productExpected.getId())
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productActual = objectMapper.readValue(resultActual, Product.class);
        Assertions.assertEquals(productExpected.getId(), productActual.getId());
        Assertions.assertEquals(productDTO.getPrice(), productActual.getPrice());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updateNotValidBodyTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String resultExpected = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productExpected = objectMapper.readValue(resultExpected, Product.class);
        productDTO.setPrice(null);
        this.mockMvc.perform(
                        put(path + "/" + productExpected.getId())
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updateProductNotAuthorizedTest() {
        this.mockMvc.perform(
                        put(path + "/" + 99))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updateProductNotFoundTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        this.mockMvc.perform(
                        put(path + "/" + 99)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void updateProductUserIsNotOwnerTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String resultExpected = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productExpected = objectMapper.readValue(resultExpected, Product.class);
        productDTO.setPrice(120.0);
        this.mockMvc.perform(
                        put(path + "/" + productExpected.getId())
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("user:user".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void getAllTestSearch() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String resultExpected = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productExpected = objectMapper.readValue(resultExpected, Product.class);
        String actualResult = this.mockMvc.perform(
                        get(path)
                                .param("page", String.valueOf(1))
                                .param("pageSize", String.valueOf(1))
                                .param("search", "test"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(PageableModel.class, Product.class);
        PageableModel<Product> response = objectMapper.readValue(actualResult, type);
        Assertions.assertEquals(1, response.getTotalObject());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertTrue(response.getObjects().contains(productExpected));
    }

    @Test
    @SneakyThrows
    @Transactional
    public void getAllTestNoSearch() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String resultExpected = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productExpected = objectMapper.readValue(resultExpected, Product.class);
        String actualResult = this.mockMvc.perform(
                        get(path)
                                .param("page", String.valueOf(1))
                                .param("pageSize", String.valueOf(1)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructParametricType(PageableModel.class, Product.class);
        PageableModel<Product> response = objectMapper.readValue(actualResult, type);
        Assertions.assertEquals(1, response.getTotalObject());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertTrue(response.getObjects().contains(productExpected));
    }

    @Test
    @SneakyThrows
    @Transactional
    public void deleteProductTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String resultExpected = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productExpected = objectMapper.readValue(resultExpected, Product.class);
        this.mockMvc.perform(
                        delete(path + "/" + productExpected.getId())
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes())))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void deleteProductNotExists() {
        this.mockMvc.perform(
                        delete(path + "/" + 99)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes())))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void deleteProductNotAuthorized() {
        this.mockMvc.perform(
                        delete(path + "/" + 99))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void deleteProductUserIsNotOwner(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String result = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product product = objectMapper.readValue(result, Product.class);
        this.mockMvc.perform(
                        delete(path + "/" + product.getId())
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("user:user".getBytes())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void likeProductTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String resultExpected = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productExpected = objectMapper.readValue(resultExpected, Product.class);
        this.mockMvc.perform(
                        get(path + "/" + productExpected.getId() + "/like")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes())))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void likeProductNotFoundTest() {
        this.mockMvc.perform(
                        get(path + "/" + 99 + "/like")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes())))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void likeProductNotAuthorizedTest() {
        this.mockMvc.perform(
                        get(path + "/" + 99 + "/like"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void dislikeProductTest() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("test name");
        productDTO.setDescription("test description");
        productDTO.setPrice(100.0);
        String resultExpected = this.mockMvc.perform(
                        post(path)
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Product productExpected = objectMapper.readValue(resultExpected, Product.class);
        this.mockMvc.perform(
                        get(path + "/" + productExpected.getId() + "/dislike")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes())))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void dislikeProductNotFoundTest() {
        this.mockMvc.perform(
                        get(path + "/" + 99 + "/dislike")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes())))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void dislikeProductNotAuthorizedTest() {
        this.mockMvc.perform(
                        get(path + "/" + 99 + "/dislike"))
                .andExpect(status().isUnauthorized());
    }
}
