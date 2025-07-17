package tech.mms.cos.core.auth.model;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;
    private long expirationMs; // 24 hrs
    private String issuer;
    private String audience;

    public String getSecret() {
        return secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }
}
