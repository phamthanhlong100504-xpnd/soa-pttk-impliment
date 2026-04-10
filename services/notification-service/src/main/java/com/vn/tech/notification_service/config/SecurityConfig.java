package com.vn.tech.notification_service.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF để dùng Postman không bị lỗi
            .authorizeHttpRequests(auth -> auth
                // Cấu hình MỞ KHÓA cho API gửi email
                .requestMatchers("/api/v1/**").permitAll()
                // Các API khác vẫn bắt buộc phải đăng nhập
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
