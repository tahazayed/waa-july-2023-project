package com.example.alumni.aspect;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.**.**.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Executing method: {}", methodName);


        Object result;
        try {
            result = joinPoint.proceed();
            logger.info("Method {} executed successfully", methodName);
        } catch (Exception ex) {
            logger.error("Error executing method {}: {}", methodName, ex.getMessage());
            throw ex;
        }

        return result;
    }
}