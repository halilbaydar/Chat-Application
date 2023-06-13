package com.chat.filter;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static com.chat.exception.CustomExceptionHandler.getExceptionResponse;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

    @SneakyThrows
    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse response, final FilterChain filterChain) {
        try {

            Authentication authentication = generateAuthentication(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            getExceptionResponse(response, HttpStatus.UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(httpServletRequest, response);
    }

    private Authentication generateAuthentication(HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getHeader("username");

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = getAuthorities(httpServletRequest);

        return new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
    }

    @NotNull
    private static List<SimpleGrantedAuthority> getAuthorities(HttpServletRequest httpServletRequest) {
        return Arrays.stream(httpServletRequest
                .getHeader("authorities").split(",")).map(SimpleGrantedAuthority::new).toList();
    }
}