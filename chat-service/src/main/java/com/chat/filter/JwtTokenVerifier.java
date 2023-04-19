package com.chat.filter;

import com.chat.config.JwtConfig;
import com.chat.interfaces.service.JwtService;
import com.chat.util.StringUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.chat.constant.ErrorConstant.ErrorStatus.UNAUTHORIZED;
import static com.chat.exception.CustomExceptionHandler.getExceptionResponse;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;

    //@SneakyThrows
    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authorizationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());
            if (StringUtil.isBlank(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
                getExceptionResponse(response, UNAUTHORIZED);
                return;
            }
            final String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
            Claims claims = jwtService.getBody(token);
            String username = claims.getSubject();
            Authentication authentication = generateAuthentication(token, username);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            getExceptionResponse(response, UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(httpServletRequest, response);
    }

    private Authentication generateAuthentication(String token, String username) {
        List<Map<String, String>> authorities = (List<Map<String, String>>) jwtService.getBody(token).get("authorities");

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toList());

        isValidRole(username, simpleGrantedAuthorities);

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                simpleGrantedAuthorities
        );
    }

    private void isValidRole(String username, List<SimpleGrantedAuthority> authorities) {
        jwtService.isValidRole(username, authorities);
    }
}