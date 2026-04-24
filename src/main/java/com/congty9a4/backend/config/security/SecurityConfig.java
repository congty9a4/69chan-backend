package com.congty9a4.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

        @Autowired
        private JwtService jwtService;

        private static final String[] WHITELIST = {
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api/auth/**",
                        "/api/users/create",
                        "/api/sample/**",
                        "/api/files/**",
                        "/",
                        "/api/v1/**",
                        "/actuator/**"
        };

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(authorize -> authorize.requestMatchers(WHITELIST).permitAll()
                                                // Open for Test chat
                                                .requestMatchers("/test-chat.html", "/ws-69chan/**").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterAfter(new JwtAuthFilter(jwtService), BasicAuthenticationFilter.class)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(new JwtAuthEntryPoint()));
                return http.build();
        }

}
