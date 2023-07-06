package com.chat.filter;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";
    private static final String DEFAULT_SCOPE_PREFIX = "SCOPE_";
    private static final String ROLES_CLAIM = "authorities";
    private static final String SCOPED_ROLES_CLAIM = "scoped-authorities";

    private final UserDetailsService userDetailsService;

    @SneakyThrows
    public static void getExceptionResponse(final HttpServletResponse response, final HttpStatus httpStatus) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        var map = new HashMap<String, Object>();
        map.put("code", httpStatus);
        map.put("status", HttpStatus.FORBIDDEN);
        response.getWriter().write(map.toString());
    }

    @NotNull
    private static Stream<SimpleGrantedAuthority> getAuthorities(final HttpServletRequest httpServletRequest) {
        return Arrays.stream(getClaims(httpServletRequest))
                .map(SimpleGrantedAuthority::new);
    }

    @NotNull
    private static String[] getClaims(HttpServletRequest httpServletRequest) {
        return httpServletRequest
                .getHeader(ROLES_CLAIM)
                .split(",");
    }

    @NotNull
    private static Stream<SimpleGrantedAuthority> getScopedAuthorities(final HttpServletRequest httpServletRequest) {
        return Arrays.stream(Optional
                        .of(httpServletRequest
                                .getHeader(SCOPED_ROLES_CLAIM)
                        ).map(scopedRoles -> scopedRoles.split(","))
                        .orElse(new String[0])
                )
                .map(SimpleGrantedAuthority::new);
    }

    @NotNull
    private static List<GrantedAuthority> getCombinedAuthorities(final HttpServletRequest httpServletRequest) {
        return Stream.concat(getAuthorities(httpServletRequest),
                        getScopedAuthorities(httpServletRequest))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse response, final FilterChain filterChain) {
        try {
            final Authentication authentication = extractAuthentication(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            getExceptionResponse(response, HttpStatus.UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(httpServletRequest, response);
    }

    private Authentication extractAuthentication(final HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getHeader("username");
        return Optional.of(userDetailsService.loadUserByUsername(username)).map(userDetails -> {
            return new UsernamePasswordAuthenticationToken(userDetails, null, getCombinedAuthorities(httpServletRequest));
        }).orElseThrow(() -> new BadCredentialsException("User could not be found!"));
    }
}