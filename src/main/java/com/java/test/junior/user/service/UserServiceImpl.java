package com.java.test.junior.user.service;

import com.java.test.junior.user.exception.UserAlreadyExistsException;
import com.java.test.junior.user.mapper.UserMapper;
import com.java.test.junior.user.model.User;
import com.java.test.junior.user.model.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(UserDTO userDTO) {
        if (userMapper.existsByUsername(userDTO.getUsername())) {
            throw new UserAlreadyExistsException(userDTO.getUsername());
        }
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(userMapper.findRole("USER"));
        userMapper.insert(user);
        return user;
    }
}
