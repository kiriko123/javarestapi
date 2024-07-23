package com.example.shopapp.configuration;

import com.example.shopapp.filter.JwtTokenFilter;
import com.example.shopapp.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/webjars/**"
                            )
                            .permitAll()

                            .requestMatchers(GET, String.format("%s/categories/**", apiPrefix)).hasAnyRole("ADMIN", "USER")
                            .requestMatchers(POST, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(GET, String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(POST, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(PUT, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(DELETE, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(POST, String.format("%s/orders/**", apiPrefix)).hasAnyRole("USER")
                            .requestMatchers(PUT, String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(GET, String.format("%s/orders/**", apiPrefix)).hasAnyRole("ADMIN", "USER")
                            .requestMatchers(DELETE, String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")

                            .requestMatchers(POST, String.format("%s/order_details/**", apiPrefix)).hasAnyRole("USER")
                            .requestMatchers(PUT, String.format("%s/order_details/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(GET, String.format("%s/order_details/**", apiPrefix)).hasAnyRole("ADMIN", "USER")
                            .requestMatchers(DELETE, String.format("%s/order_details/**", apiPrefix)).hasRole("ADMIN")

                            .anyRequest().permitAll();
                });
        return http.build();
    }
}
