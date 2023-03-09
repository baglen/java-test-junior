package com.java.test.junior.user.mapper;

import com.java.test.junior.user.model.Role;
import com.java.test.junior.user.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@Mapper
public interface UserMapper {
    User findByUsername(String username);
    void insert(User user);
    Role findRole(String role);
    Boolean existsByUsername(String username);
}
