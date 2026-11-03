package tutorconecta.com.example.tutorconectaapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {
    private final JwtEncoder jwtEncoder;
    private final long expirationMs;

    public JwtTokenService(
            JwtEncoder jwtEncoder,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.jwtEncoder = jwtEncoder;
        this.expirationMs = expirationMs;
    }

    public String generateToken(Authentication authentication) {
        Instant issuedAt = Instant.now();
        String authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(","));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusMillis(expirationMs))
                .claim("roles", authorities)
                .claim("authorities", authorities)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS512).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
