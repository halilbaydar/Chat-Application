package com.chat.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.chat.Constants.CORRELATION_ID_HEADER;
import static com.chat.Constants.CORRELATION_ID_KEY;


@Component
public class MDCHandlerInterceptor implements HandlerInterceptor {

    private final IdGenerator idGenerator;

    public MDCHandlerInterceptor(IdGenerator generator) {
        this.idGenerator = generator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (StringUtils.hasLength(correlationId)) {
            MDC.put(CORRELATION_ID_KEY, correlationId);
        } else {
            MDC.put(CORRELATION_ID_KEY, getNewCorrelationId());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        MDC.remove(CORRELATION_ID_KEY);
    }

    private String getNewCorrelationId() {
        return idGenerator.generateId().toString();
    }
}
