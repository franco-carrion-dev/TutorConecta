package tutorconecta.com.example.tutorconectaapi.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Arrays;
import java.util.List;

public class CustomJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String authorities = jwt.getClaimAsString("roles");
        if (authorities == null) {
            authorities = jwt.getClaimAsString("authorities");
        }
        List<SimpleGrantedAuthority> grantedAuthorities = authorities == null
                ? List.of()
                : Arrays.stream(authorities.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtAuthenticationToken(jwt, grantedAuthorities, jwt.getSubject());
    }
}
