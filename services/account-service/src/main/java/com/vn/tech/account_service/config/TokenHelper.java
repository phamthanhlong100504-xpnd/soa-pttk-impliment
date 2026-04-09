package com.vn.tech.account_service.config;

import java.util.Date;
import java.util.UUID;

import com.vn.tech.account_service.entity.AccountEntity;
import com.vn.tech.account_service.exception.AppException;
import com.vn.tech.account_service.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenHelper {
    private static final String SECRET_KEY = "yourSecretKeyyourSecretKeyyourSecretKeyyourSecretKeyyourSecretKeyyourSecretKeyyourSecretKeyyourSecretKeyyourSecretKeyyourSecretKeyyourSecretKeyyourSecretKey";
    private static final long EXPIRATION_TIME_ACCESS_TOKEN = 900_000; // 10 days
    private static final long EXPIRATION_TIME_REFRESH_TOKEN = 864_000_000;

    public static String generateAccessToken(AccountEntity account) {
        Date now = new Date();
        Date expirrationDate = new Date(now.getTime() + EXPIRATION_TIME_ACCESS_TOKEN);

        return Jwts.builder()
            .claim("user_id",account.getId())
            .claim("email",account.getEmail())
            .claim("role", account.getRole())
            .setSubject(account.getEmail())
            .setIssuedAt(now)
            .setExpiration(expirrationDate)
            .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
            .compact();
    }

    public static String generateRefreshToken(AccountEntity account) {
        Date now = new Date();
        Date expirrationDate = new Date(now.getTime() + EXPIRATION_TIME_REFRESH_TOKEN);

        return Jwts.builder()
            .claim("user_id",account.getId())
            .claim("email",account.getEmail())
            .claim("role", account.getRole())
            .setSubject(account.getEmail())
            .setIssuedAt(now)
            .setExpiration(expirrationDate)
            .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
            .compact();
    }

    public static UUID getAccountIdFromToken(String accessToken) {
        accessToken = accessToken.substring(7);
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(accessToken)
                .getBody();
            return claims.get("user_id", UUID.class);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    public static String getEmailFromToken(String accessToken) {
        accessToken = accessToken.substring(7);
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(accessToken)
                .getBody();
            return claims.get("email", String.class);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    public static String getRoleFromToken(String accessToken) {
        accessToken = accessToken.substring(7);
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(accessToken)
                .getBody();
            return claims.get("role", String.class);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    private static Claims parseToken(String token) {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();
    }

    public static boolean validateAccessToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new AppException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
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
