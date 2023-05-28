package com.chat.filter;

import com.chat.model.entity.message.RabbitUserEntity;
import com.chat.rabbit.RabbitUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.chat.constant.GeneralConstant.ACTIVE_USER;
import static com.chat.util.SessionUtil.getAttribute;
import static com.chat.util.SessionUtil.setAttribute;

@Component
@RequiredArgsConstructor
public class UserSetFilter extends OncePerRequestFilter {
    private final RabbitUserService rabbitUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String username = request.getHeader("username");
        RabbitUserEntity rabbitUserEntity = getAttribute(ACTIVE_USER);
        if (rabbitUserEntity == null) {
            rabbitUserEntity = rabbitUserService.receiveUserFromUserService(username);
            setAttribute(ACTIVE_USER, rabbitUserEntity);
        }

        if (rabbitUserEntity == null) {
            throw new UsernameNotFoundException(String.format("username not fount %s", username));
        }
        filterChain.doFilter(request, response);
    }
}
