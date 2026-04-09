package com.vn.tech.account_service.config;

import java.util.Date;
import java.util.UUID;

import com.vn.tech.account_service.entity.AccountEntity;
import com.vn.tech.account_service.exception.AppException;
import com.vn.tech.account_service.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

public class TokenHelper {
    @Value("${app.jwt.secret}")
    private static String secretKey;

    @Value("${app.jwt.expiration-access}")
    private static long expirationTimeAccessToken;

    @Value("${app.jwt.expiration-refresh}")
    private static long expirationTimeRefreshToken;

    public static String generateAccessToken(AccountEntity account) {
        Date now = new Date();
        Date expirrationDate = new Date(now.getTime() + expirationTimeAccessToken);

        return Jwts.builder()
            .claim("user_id",account.getId())
            .claim("email",account.getEmail())
            .claim("role", account.getRole())
            .setSubject(account.getEmail())
            .setIssuer("user-issue")
            .setIssuedAt(now)
            .setExpiration(expirrationDate)
            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
            .compact();
    }

    public static String generateRefreshToken(AccountEntity account) {
        Date now = new Date();
        Date expirrationDate = new Date(now.getTime() + expirationTimeRefreshToken);

        return Jwts.builder()
            .claim("user_id",account.getId())
            .claim("email",account.getEmail())
            .claim("role", account.getRole())
            .setSubject(account.getEmail())
            .setIssuedAt(now)
            .setExpiration(expirrationDate)
            .signWith(SignatureAlgorithm.HS512,secretKey)
            .compact();
    }

    public static UUID getAccountIdFromToken(String accessToken) {
        accessToken = accessToken.substring(7);
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(accessToken)
                .getBody();
            return claims.get("user_id", UUID.class);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    private static Claims parseToken(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }

    public static boolean validateRefreshToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }
}
