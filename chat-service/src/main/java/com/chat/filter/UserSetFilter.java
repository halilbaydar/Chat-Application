package com.chat.filter;

import com.chat.constant.ErrorConstant;
import com.chat.exception.CustomException;
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

import static com.chat.constant.ErrorConstant.ErrorMessage.*;
import static com.chat.constant.GeneralConstant.ACTIVE_USER;
import static com.chat.exception.CustomExceptionHandler.getExceptionResponse;
import static com.chat.util.StringUtil.isBlank;

@Component
@RequiredArgsConstructor
public class UserSetFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (!isBlank(token)) {
                String username;

                try {
                    username = jwtService.getUsernameFromToken(token);
                } catch (Exception e) {
                    throw new CustomException(INVALID_TOKEN);
                }

                UserEntity activeUser = (UserEntity) request.getSession().getAttribute(ACTIVE_USER);
                if (activeUser == null) {
                    activeUser = userRepository.findByUsername(username);
                    if (activeUser == null)
                        throw new CustomException(USER_NOT_EXIST);
                    request.getSession().setAttribute(ACTIVE_USER, activeUser);
                }

                if (!activeUser.getUsername().equals(username))
                    throw new CustomException(INVALID_OPERATION);
            }
        } catch (Exception ex) {
            if (ex instanceof CustomException) {
                getExceptionResponse(response, ((CustomException) ex).getStatus().name());
            } else {
                getExceptionResponse(response, ErrorConstant.ErrorStatus.UNAUTHORIZED);
            }
            return;
        }
        filterChain.doFilter(request, response);
    }
}
