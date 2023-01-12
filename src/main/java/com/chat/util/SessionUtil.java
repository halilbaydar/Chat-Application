package com.chat.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

import static com.chat.constant.ErrorConstant.ErrorMessage.INVALID_OPERATION;
import static com.chat.constant.GeneralConstant.ACTIVE_USER;

public final class SessionUtil {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if (request == null) {
            throw new RuntimeException(INVALID_OPERATION);
        }
        return request;
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static <U, S extends Serializable> U getAttribute(S s) {
        return (U) getSession().getAttribute(s.toString());
    }

    public static <V extends Serializable> void setAttribute(String s, V v) {
        getSession().setAttribute(s, v);
    }

    public static void removeAttribute(String s) {
        getSession().removeAttribute(s);
    }

    public static <T> T getActiveUser() {
        return getAttribute(ACTIVE_USER);
    }

}
