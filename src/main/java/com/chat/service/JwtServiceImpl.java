package com.chat.service;

import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.JwtService;
import com.chat.model.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;
    private final UserRepository userRepository;

    @Override
    public Claims getBody(String token) {
        return extractClaims(token).getBody();
    }

    public Jws<Claims> extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractClaims(token).getBody());
    }

    @Override
    public boolean isValidRole(String username, List<SimpleGrantedAuthority> authorities) {
        UserEntity activeUser = userRepository.findByUsername(username);
        if (activeUser == null) {
            return false;
        }
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = activeUser.getRole().getGrantedAuthorities();
        return simpleGrantedAuthorities.equals(authorities);
    }

    @Override
    public String generateLoginJwtToken(String username, Collection<? extends GrantedAuthority> authorities, String tokenId, Date date) {
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", authorities)
                .setIssuedAt(date)
                .setId(tokenId)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateTokenForLogin(String name, Collection<? extends GrantedAuthority> authorities, String tokenId) {
        return this.generateLoginJwtToken(name, authorities, tokenId, new Date());
    }

    @Override
    public String generateTokenForLogin(String name, Collection<? extends GrantedAuthority> authorities) {
        return this.generateTokenForLogin(name, authorities, UUID.randomUUID().toString());
    }
}
