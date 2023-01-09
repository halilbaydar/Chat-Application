package com.chat.filter;

import com.chat.interfaces.repository.UserRepository;
import com.chat.interfaces.service.JwtService;
import com.chat.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.chat.constant.ErrorConstant.*;
import static com.chat.constant.GeneralConstant.ACTIVE_USER;
import static com.chat.util.StringUtil.isBlank;

@Component
@RequiredArgsConstructor
public class UserSetFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        request.getSession().setAttribute("sessionId", request.getSession().getId());
        request.getSession().setAttribute("requestId", UUID.randomUUID().toString());

        if (!isBlank(token)) {
            String username = null;

            try {
                username = jwtService.getUsernameFromToken(token);
            } catch (Exception e) {
                throw new RuntimeException(INVALID_TOKEN);
            }

            UserEntity activeUser = (UserEntity) request.getSession().getAttribute(ACTIVE_USER);
            if (activeUser == null) {
                activeUser = userRepository.findByUsername(username);
                if (activeUser == null)
                    throw new RuntimeException(USER_NOT_EXIST);
                request.getSession().setAttribute(ACTIVE_USER, activeUser);
            }

            if (!activeUser.getUsername().equals(username))
                throw new RuntimeException(INVALID_OPERATION);
        }

        filterChain.doFilter(request, response);
    }
}
