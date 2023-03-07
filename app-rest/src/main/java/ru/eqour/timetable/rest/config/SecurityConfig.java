package ru.eqour.timetable.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable();
        http.authorizeHttpRequests(customizer -> customizer
                .antMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
