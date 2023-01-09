package com.chat.interfaces.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public interface JwtService {

    Claims getBody(String token);

    Jws<Claims> extractClaims(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    boolean isValidRole(String username, List<SimpleGrantedAuthority> authorities);

    String generateLoginJwtToken(String username, Collection<? extends GrantedAuthority> authorities, String tokenId, Date date);

    String generateTokenForLogin(String name, Collection<? extends GrantedAuthority> authorities, String tokenId);

    String generateTokenForLogin(String name, Collection<? extends GrantedAuthority> authorities);
}
