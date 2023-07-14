package com.chat.auth;

import com.chat.constant.HttpConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AuthUtil {
    public abstract SecretKey getKey();

    public abstract Claims getBody(String token);

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(removeBearer(token)).getBody();
    }

    public boolean isTokenExpired(Claims claims, Date date) {
        return claims.getExpiration() != null && claims.getExpiration().before(date);
    }

    public boolean isInvalid(Claims claims) {
        return this.isTokenExpired(claims, new Date());
    }

    public List<String> getGrantedAuthorities(Claims body) {
        List<Map<String, String>> authorities = (List<Map<String, String>>) body.get(HttpConstant.JWT_AUTH_SUBJECT);
        return authorities.stream()
                .map(m -> m.get("authority")).collect(Collectors.toList());
    }

    private String removeBearer(@NotBlank String token) {
        return token.replaceFirst("Bearer ", "");
    }


    public Authentication generateAuthentication(String username, List<SimpleGrantedAuthority> simpleGrantedAuthorities) {
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                simpleGrantedAuthorities
        );
    }
}
