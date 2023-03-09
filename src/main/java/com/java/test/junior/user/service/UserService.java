package com.java.test.junior.user.service;

import com.java.test.junior.user.model.User;
import com.java.test.junior.user.model.UserDTO;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
public interface UserService {

    /**
     * @param userDTO is a user to be created
     * @return the user created from the database
     */
    User register(UserDTO userDTO);
}
