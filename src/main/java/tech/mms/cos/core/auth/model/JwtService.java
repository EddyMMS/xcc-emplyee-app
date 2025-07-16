package tech.mms.cos.core.auth.model;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import tech.mms.cos.core.model.LocalAppAccount;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    public String generateToken(LocalAppAccount account) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + jwtConfig.getExpirationMs());


        return Jwts.builder()
                .subject(account.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .issuer(jwtConfig.getIssuer())
                .audience().add(jwtConfig.getAudience()).and()
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(jwtConfig.getIssuer())
                .requireAudience(jwtConfig.getAudience())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.getSubject();
    }
}
