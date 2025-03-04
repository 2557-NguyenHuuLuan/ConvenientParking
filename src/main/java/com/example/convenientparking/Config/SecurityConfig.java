package com.example.convenientparking.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/","/intro" ,"/register", "/login","/password/**", "/css/**", "/js/**","/lib/**", "/Logo/**", "/HomeImages/**", "/CarImages/**").permitAll()
                        .requestMatchers("/admin/**")
                        .hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers("/revenue/**", "/spot/**", "/package/**", "/user-statistics/**","/vehicle-statistic/**","/admin/vehicle-types/**")
                        .hasAnyAuthority("EMPLOYEE")
                        .requestMatchers("/deposit", "/vehicles/**", "/parking/**", "/rental/**")
                        .hasAnyAuthority( "USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureHandler(failureHandler())
                        .defaultSuccessUrl("/", true) // Chuyển về trang chủ sau khi đăng nhập thành công
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Đường dẫn xử lý yêu cầu đăng xuất
                        .logoutSuccessUrl("/login?logout") // Chuyển hướng sau khi đăng xuất thành công
                        .invalidateHttpSession(true) // Hủy session
                        .deleteCookies("JSESSIONID") // Xóa cookie phiên làm việc
                        .permitAll()
                );
        return http.build();
    }


}
