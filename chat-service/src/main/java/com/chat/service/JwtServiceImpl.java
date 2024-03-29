package com.chat.service;

import com.chat.interfaces.service.JwtService;
import com.chat.model.common.Role;
import com.chat.model.entity.message.RabbitUserEntity;
import com.chat.rabbit.RabbitUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

import static com.chat.constant.ErrorConstant.ErrorMessage.INVALID_OPERATION;
import static com.chat.util.StringUtil.isBlank;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;
    private final RabbitUserService rabbitUserService;

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
        RabbitUserEntity activeUser = rabbitUserService.receiveUserFromUserService(username);
        if (activeUser == null) {
            return false;
        }
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = Role.valueOf(activeUser.getRole()).getGrantedAuthorities();
        return Arrays.equals(authorities.toArray(), simpleGrantedAuthorities.toArray());
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

    @Override
    public String getUsernameFromToken(String token) {
        if (isBlank(token)) throw new RuntimeException(INVALID_OPERATION);
        return getBody(removeBearer(token)).getSubject();
    }

    private String removeBearer(String token) {
        if (isBlank(token)) throw new RuntimeException(INVALID_OPERATION);
        return token.replaceFirst("Bearer ", "");
    }

    @Override
    public List<Map<String, String>> getAuthorities(String token) {
        if (isBlank(token)) throw new RuntimeException(INVALID_OPERATION);
        return (List<Map<String, String>>) getBody(token).get("authorities");
    }
}
