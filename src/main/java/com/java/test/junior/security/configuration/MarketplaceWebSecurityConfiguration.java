package com.java.test.junior.security.configuration;

import com.java.test.junior.security.service.DefaultUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author artiom.spac
 * @version java-test-junior
 * @apiNote 08.03.2023
 */
@Configuration
@EnableWebSecurity
public class MarketplaceWebSecurityConfiguration {

    private final DefaultUserDetailsService defaultUserDetailsService;

    public MarketplaceWebSecurityConfiguration(DefaultUserDetailsService defaultUserDetailsService) {
        this.defaultUserDetailsService = defaultUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/products/**").fullyAuthenticated()
                .requestMatchers(HttpMethod.POST, "/loading/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/products/**").fullyAuthenticated()
                .requestMatchers(HttpMethod.DELETE, "/products/**").fullyAuthenticated()
                .requestMatchers(HttpMethod.GET, "/products/*/like").fullyAuthenticated()
                .requestMatchers(HttpMethod.GET, "/products/*/dislike").fullyAuthenticated()
                .anyRequest().permitAll()
                .and().httpBasic();
        http.authenticationProvider(authenticationProvider());
        http.csrf().disable();
        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(defaultUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
