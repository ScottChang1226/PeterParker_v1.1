package com.tibame.peterparker.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private SecretKey SECRET_KEY;

    // 注入密鑰或生成自動密鑰
    @Value("${jwt.secret-key:}")
    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = (secretKey != null && !secretKey.isEmpty())
                ? Keys.hmacShaKeyFor(secretKey.getBytes())
                : Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // 從 token 中提取用戶名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 從 token 中提取過期時間
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 泛型方法，用於從 token 中提取特定的 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 從 token 中提取所有的 claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 檢查 token 是否已過期
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 生成 token，帶入使用者名稱
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // 生成 token，帶入 UserDetails
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    // 創建一個新的 token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 設置 10 小時過期
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 驗證 token 的有效性
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 驗證 token 的有效性，帶入用戶名
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
