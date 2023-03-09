package com.java.test.junior.security.service;

import com.java.test.junior.security.details.DefaultUserDetails;
import com.java.test.junior.user.mapper.UserMapper;
import com.java.test.junior.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@Service
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    public DefaultUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
        return new DefaultUserDetails(user);
    }
}
