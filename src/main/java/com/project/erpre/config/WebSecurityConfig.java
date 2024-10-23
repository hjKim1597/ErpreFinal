package com.project.erpre.config;

import com.project.erpre.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // 회원가입이 생략되어 평문 비밀번호를 사용하고 있으므로 BCryptPasswordEncoder 제거
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors() // CORS 설정 활성화
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 기반으로 설정
                .and()
                .authorizeRequests()
                    .antMatchers("/",  "/static/**", "/bundle/**", "/img/**", "/css/**", "/fonts/**", "/index.html").permitAll()
                    .antMatchers("/login", "/**").permitAll() // 로그인 앤드포인트 허용 (현재 모든 페이지 접근 허용! 이거 나중에 바꿔야 함)
                    .antMatchers("/user/**").hasAnyRole("Staff", "Admin", "Assistant Manager", "Executive", "Director", "Manager")
                    .antMatchers("/admin/**").hasRole("Admin")
                    .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
                .and()
                .formLogin() // 기본 로그인 폼 제공
                    .loginPage("/login")
                    .defaultSuccessUrl("/main", true)
                    .permitAll()
                .and()
                .logout() // 로그아웃 처리
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .permitAll();

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
