package com.tibame.peterparker.util;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
// 主要職責是 提取JWT 和 驗證JWT
// 加載用戶詳細信息
// 設置 Spring Security 的 Authentication
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    // 過濾請求並進行 JWT 驗證
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 從標頭中提取 JWT
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // 去掉 "Bearer " 前綴
            try {
                username = jwtUtil.extractUsername(jwt); // 解析 JWT 並提取 username
            } catch (ExpiredJwtException e) {
                System.out.println("JWT has expired");
            }
        }

        // 如果已經有使用者資訊並且 JWT 驗證通過
        if (username != null && jwtUtil.validateToken(jwt, username)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // 調用 AdminUserDetailsService
            if (userDetails != null) {
                // 建立用戶的認證信息，並存入 SecurityContext
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 將認證信息存入 SecurityContext 以便後續的安全性檢查
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // 繼續執行過濾鏈
        chain.doFilter(request, response);
    }
}
