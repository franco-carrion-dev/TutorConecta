package tutorconecta.com.example.tutorconectaapi.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CustomJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Object roleClaim = jwt.getClaim("roles");
        if (roleClaim == null) {
            roleClaim = jwt.getClaim("authorities");
        }

        List<String> roles;
        if (roleClaim instanceof String value) {
            roles = Arrays.stream(value.split(",")).toList();
        } else if (roleClaim instanceof Collection<?> values) {
            roles = values.stream().filter(String.class::isInstance)
                    .map(String.class::cast).toList();
        } else {
            roles = List.of();
        }

        List<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .map(value -> value.startsWith("ROLE_") ? value : "ROLE_" + value)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtAuthenticationToken(jwt, grantedAuthorities, jwt.getSubject());
    }
}
