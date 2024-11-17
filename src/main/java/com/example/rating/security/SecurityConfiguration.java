package com.example.rating.security;

import com.example.rating.entity.User;
import com.example.rating.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        if (userService.findUserByUsername("user1").isEmpty()) {
            User user = new User();
            user.setUsername("user1");
            user.setPassword(passwordEncoder().encode("password")); // Plaintext; will be hashed
            user.setRole("NORMAL_USER");
            userService.saveUser(user);
        }

        if (userService.findUserByUsername("mod1").isEmpty()) {
            User user = new User();
            user.setUsername("mod1");
            user.setPassword(passwordEncoder().encode("password-mod")); // Plaintext; will be hashed
            user.setRole("MODERATOR");
            userService.saveUser(user);
        }
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    SecurityFilterChain basicFilterChain(HttpSecurity http, AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        configureDefaultSecurity(http, authenticationManagerBuilder);
        return http.build();
    }

    protected void configureDefaultSecurity(HttpSecurity http, AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver = request -> authenticationManagerBuilder.getObject();
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(it -> it
            .anyRequest().authenticated());
        http.httpBasic(Customizer.withDefaults());
        http.addFilterBefore(getAuthenticationFilter(authenticationManagerResolver), UsernamePasswordAuthenticationFilter.class);
    }

    public AuthenticationFilter getAuthenticationFilter(AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver) {
        AuthenticationConverter converter = request -> new UsernamePasswordAuthenticationToken(request.getHeader("username"), request.getHeader("password"));
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManagerResolver, converter);
        filter.setRequestMatcher(new AntPathRequestMatcher("/admin-login", "POST"));
        filter.setSuccessHandler((request, response, authentication) -> {});
        return filter;
    }

}
