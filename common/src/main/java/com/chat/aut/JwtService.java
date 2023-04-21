package com.chat.aut;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    Claims getBody(String token);

    Jws<Claims> extractClaims(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    String generateLoginJwtToken(String username, Collection<? extends GrantedAuthority> authorities, String tokenId, Date date);

    String generateTokenForLogin(String name, Collection<? extends GrantedAuthority> authorities, String tokenId);

    String generateTokenForLogin(String name, Collection<? extends GrantedAuthority> authorities);

    String getUsernameFromToken(String token);

    List<Map<String, String>> getAuthorities(String token);
}
