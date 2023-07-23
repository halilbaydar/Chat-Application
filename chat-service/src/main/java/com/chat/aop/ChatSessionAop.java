package com.chat.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ChatSessionAop {

    @Pointcut("execution(* com.chat.interfaces.service.SessionService.connectSession(..))")
    public void connectSession() {

    }

    @Around("connectSession()")
    public void connectSessionAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        proceedingJoinPoint.proceed();
    }

}
