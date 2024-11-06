package com.tibame.peterparker.config;

import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.tibame.peterparker.filter.AuthRequestFilter;
import com.tibame.peterparker.config.AppConfig;
import com.tibame.peterparker.service.AdminUserDetailsService;

@Configuration // 配置類
@EnableWebSecurity // 啟用 Spring Security
public class SecurityConfig {

    @Autowired
    private AppConfig detailsService;

    @Autowired
    private AuthRequestFilter authRequestFilter;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;



    // 密碼編碼器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 身份驗證提供者，設定 UserdetailsService 和 PasswordEncoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AuthenticationManager 實例
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new ProviderManager(authenticationProvider());
    }

    // 跨域請求設定
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:8081","http://127.0.0.1:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 安全過濾鏈
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors() // 啟用 CORS
                .and().csrf().disable(); // 禁用 CSRF

        // 設定驗證的提供者
        http.authenticationProvider(authenticationProvider())
                .addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // 控制 api 使用權限
        http.authorizeRequests(authorize -> {
            authorize.antMatchers("/user/**","/user/userinfo").permitAll();
            authorize.antMatchers("/official/**").permitAll();
            authorize.antMatchers("/geocode").permitAll();//放行/geocode相關以進行測試
            authorize.antMatchers("/api/**").hasAnyRole("ADMIN");
            authorize.anyRequest().authenticated();
        });

        // 設置會話策略為無狀態（使用 JWT 時推薦的配置）
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors() // 啟用CORS
            .and()
            .csrf().disable() // 關閉 CSRF 保護
            .authorizeRequests()
            .antMatchers("/api/admin/**", "/api/user/**","/api/orders/**","/api/owner/**", "/api/statistics/**","/api/parking/**").permitAll() // 允許不需驗證的路徑
            .anyRequest().authenticated() // 其他路徑需驗證
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 無狀態的 Session

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允許發送憑證
        config.addAllowedOriginPattern("*"); // 允許所有的來源
        config.addAllowedHeader("*"); // 允許所有的請求標頭
        config.addAllowedMethod("*"); // 允許所有的請求方法
        source.registerCorsConfiguration("/**", config); // 將此配置應用於所有路徑
        return new CorsFilter(source);
    }


}
