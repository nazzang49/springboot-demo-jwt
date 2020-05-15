package com.example.demo.config;

import com.example.demo.filter.AjaxAuthenticationFilter;
import com.example.demo.filter.JwtAuthenticationFilter;
import com.example.demo.security.AjaxAuthenticationProvider;
import com.example.demo.security.BaseSecurityHandler;
import com.example.demo.security.JwtAuthenticationProvider;
import com.example.demo.security.SkipPathRequestMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private AjaxAuthenticationProvider ajaxAuthenticationProvider;

    @Autowired
    private BaseSecurityHandler baseSecurityHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void configure(WebSecurity web) {
        // resources 파일 통과
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // jwt, login authentication 분리
        auth
                .authenticationProvider(ajaxAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), FilterSecurityInterceptor.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                // login = 로그인 뷰
                // token = 인증 및 토큰 발급
                .antMatchers("/token").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/error").permitAll();
    }

    // 아래 1개 path -> 최초 로그인 시 인증 및 토큰 발급
    @Bean
    public AntPathRequestMatcher antPathRequestMatcher() {
        return new AntPathRequestMatcher("/token", HttpMethod.POST.name());
    }

    @Bean
    public AjaxAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
        AjaxAuthenticationFilter filter = new AjaxAuthenticationFilter(antPathRequestMatcher(), objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        // 인증 성공
        filter.setAuthenticationSuccessHandler(baseSecurityHandler);
        // 인증 실패 (계정 없거나 불일치)
        filter.setAuthenticationFailureHandler(baseSecurityHandler);
        return filter;
    }

    // 아래 3개 path -> JWT 필터 통과
    @Bean
    public SkipPathRequestMatcher skipPathRequestMatcher() {
        return new SkipPathRequestMatcher(Arrays.asList("/login", "/token", "error"));
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(skipPathRequestMatcher());
        filter.setAuthenticationManager(authenticationManager());
        // 인증 실패 (계정 없거나 불일치)
        filter.setAuthenticationFailureHandler(baseSecurityHandler);
        return filter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
