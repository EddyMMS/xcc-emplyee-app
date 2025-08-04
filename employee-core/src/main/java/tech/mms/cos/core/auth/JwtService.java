package tech.mms.cos.core.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import tech.mms.cos.core.auth.account.model.LocalAppAccount;

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
        .audience()
        .add(jwtConfig.getAudience())
        .and()
        .signWith(secretKey)
        .compact();
  }

  private Claims parseClaimsFromToken(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .requireIssuer(jwtConfig.getIssuer())
        .requireAudience(jwtConfig.getAudience())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public Claims validateToken(String token) {
    try {
      return parseClaimsFromToken(token);
    } catch (ExpiredJwtException e) {
      throw e;
    } catch (IncorrectClaimException e) {
      if (e.getClaimName().equalsIgnoreCase("iss")) {
        throw e;
      }

      throw e;
    } catch (Exception e) {
      throw e;
    }
  }

  public String getUsernameFromToken(String token) {
    Claims claims = parseClaimsFromToken(token);
    return claims.getSubject();
  }
}
