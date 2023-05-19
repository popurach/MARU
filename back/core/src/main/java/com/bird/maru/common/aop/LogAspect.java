package com.bird.maru.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    private static final String REQUEST_MAPPED = "[{}.{}()]---------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
    private static final String EXCEPTION_INFO = " => Exception Info      {} : {}";
    private static final String CAUSED_INFO = " => Caused Info         {} : {}";
    private static final String FINISH_LINE = "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";

    @Pointcut("execution(* com.bird.maru..controller.*Controller*.*(..))")
    private void controller() {
    }

    @Around("controller()")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        log.info(
                REQUEST_MAPPED,
                ((MethodSignature) signature).getDeclaringType().getSimpleName(), // 메서드를 정의(선언)한 클래스
                ((MethodSignature) signature).getName() // 메서드의 이름
        );
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Exception occurred in method: {}, message: {}", signature.toShortString(), e.getMessage());
            logExceptionInfo(e);
            throw e;
        }
        log.info(FINISH_LINE);
        return result;
    }

    private void logExceptionInfo(Throwable e) {
        boolean isFirst = true;

        while (e != null) {
            log.error(
                    isFirst ? EXCEPTION_INFO : CAUSED_INFO,
                    e.getClass().getSimpleName(),
                    e.getMessage()
            );

            e = e.getCause();
            isFirst = false;
        }
    }

}
