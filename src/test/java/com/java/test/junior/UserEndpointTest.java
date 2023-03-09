package com.java.test.junior;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.test.junior.user.model.User;
import com.java.test.junior.user.model.UserDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class UserEndpointTest {

    private final String path = "/users";

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

    @Test
    @SneakyThrows
    @Transactional
    public void createUserTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Artiom");
        userDTO.setLastName("Spac");
        userDTO.setUsername("login");
        userDTO.setPassword("password");
        String expectedResult = this.mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        User user = objectMapper.readValue(expectedResult, User.class);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(userDTO.getFirstName(), userDTO.getFirstName());
        Assertions.assertEquals(userDTO.getLastName(), userDTO.getLastName());
        Assertions.assertEquals(userDTO.getUsername(), userDTO.getUsername());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void createUserNotValidBody() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLastName("Spac");
        userDTO.setUsername("login");
        userDTO.setPassword("password");
        this.mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @Transactional
    public void createUserWithUsernameThatAlreadyExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Artiom");
        userDTO.setLastName("Spac");
        userDTO.setUsername("login");
        userDTO.setPassword("password");
        this.mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
        this.mockMvc.perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }


}
