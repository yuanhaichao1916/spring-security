package com.example.security.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;



@Aspect
@Slf4j
public class ScheduleExceptionHandler {


    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void proxyAspect() {
    }

    @AfterThrowing(pointcut = "proxyAspect()", throwing = "ex")
    public void doException(JoinPoint joinPoint, Exception ex) {
        Object target = joinPoint.getTarget();

        this.saveException(target, ex);

    }

    public void saveException(Object target, Exception ex) {


    }

}