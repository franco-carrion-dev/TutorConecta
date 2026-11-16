package tutorconecta.com.example.tutorconectaapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String base64Secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.signingKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(base64Secret));
        this.expirationMs = expirationMs;
    }

    public String generateToken(String username, List<String> authorities) {
        Date issuedAt = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public List<String> extractAuthorities(String token) {
        Object value = parseClaims(token).get("authorities");
        if (!(value instanceof List<?> values)) {
            return List.of();
        }
        return values.stream().filter(String.class::isInstance).map(String.class::cast).toList();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getSubject() != null
                    && claims.getExpiration() != null
                    && claims.getExpiration().after(new Date());
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
