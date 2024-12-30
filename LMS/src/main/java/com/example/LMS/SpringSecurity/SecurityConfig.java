package com.example.LMS.SpringSecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/register").permitAll()
                                .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user/editprofile").authenticated()
                        .requestMatchers("/user/profiles/{userId}").authenticated()

                        .requestMatchers("/ADMIN/**").hasAuthority("ADMIN")
                        .requestMatchers("/INSTRUCTOR/**").hasAuthority("INSTRUCTOR")
                        .requestMatchers("/STUDENT/**").hasAuthority("STUDENT")
                        .anyRequest().authenticated()  )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session, JWT-based authentication
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
