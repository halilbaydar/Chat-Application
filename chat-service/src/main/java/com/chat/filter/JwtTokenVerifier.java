package com.chat.filter;

import com.chat.interfaces.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.chat.exception.CustomExceptionHandler.getExceptionResponse;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final JwtService jwtService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse response,
                                    final FilterChain filterChain) {
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

        //List<SimpleGrantedAuthority> simpleGrantedAuthorities = ArraysUtilKt.fromString(httpServletRequest.getHeader("authorities"));

        //isValidRole(username, simpleGrantedAuthorities);

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                null//simpleGrantedAuthorities
        );
    }

    private void isValidRole(String username, List<SimpleGrantedAuthority> authorities) {
        jwtService.isValidRole(username, authorities);
    }
}