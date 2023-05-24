package com.chat.util;

import com.chat.constant.HttpConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AuthUtil {
    public abstract SecretKey getKey();

    public abstract Claims getBody(String token);

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(Claims claims, Date date) {
        return claims.getExpiration().before(date);
    }

    private boolean isTokenExpired(String token, Date date) {
        return this.getAllClaimsFromToken(token).getExpiration().before(date);
    }

    public boolean isInvalid(Claims claims) {
        return this.isTokenExpired(claims, new Date());
    }

    public boolean isInvalid(String token) {
        return this.isTokenExpired(token, new Date());
    }

    public List<SimpleGrantedAuthority> getGrantedAuthorities(Claims body) {
        List<Map<String, String>> authorities = (List<Map<String, String>>) body.get(HttpConstant.JWT_AUTH_SUBJECT);
        return authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toList());
    }

    public Authentication generateAuthentication(String token, String username) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = getGrantedAuthorities(getBody(token));
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                simpleGrantedAuthorities
        );
    }

    public Authentication generateAuthentication(String username, List<SimpleGrantedAuthority> simpleGrantedAuthorities) {
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                simpleGrantedAuthorities
        );
    }
}
