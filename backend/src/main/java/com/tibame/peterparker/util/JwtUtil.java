package com.tibame.peterparker.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private String SECRET_KEY = "your_secret_key";

    // 生成 JWT Token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token 有效時間 10 小時
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 從 Token 中提取用戶名
    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        // 這個方法負責解析 JWT Token，並提取出 Token 中的 主體（Subject），也就是我們在 generateToken 中存入的用戶名。
    }

    // 驗證 JWT Token 的有效性
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
        // username.equals(extractUsername(token))：先驗證從 Token 中提取的用戶名是否與當前請求的用戶名匹配。
        // !isTokenExpired(token)：再驗證 Token 是否已經過期。如果 Token 還未過期且用戶名正確，那麼這個 Token 是有效的。
    }

    // 檢查 Token 是否過期
    private boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration().before(new Date());
        // 這個方法會檢查 Token 中的過期時間，並確認該時間是否已經早於當前時間。如果 Token 已經過期，則返回 true，否則返回 false。
    }
}


