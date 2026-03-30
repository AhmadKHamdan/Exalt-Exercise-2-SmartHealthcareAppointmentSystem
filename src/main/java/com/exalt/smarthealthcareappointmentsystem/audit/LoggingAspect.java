package com.exalt.smarthealthcareappointmentsystem.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterReturning(value = "@annotation(logAction)")
    public void logAction(JoinPoint joinPoint, LogAction logAction) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Action: " + logAction.value() + " | Method: " + joinPoint.getSignature().getName()
                + " | By: " + auth.getName() + " | Role: " + auth.getAuthorities().stream().toList().get(0));

        logger.info("Action: " + logAction.value() + " | Method: " + joinPoint.getSignature().getName()
                + " | By: " + auth.getName() + " | Role: " + auth.getAuthorities().stream().toList().get(0));
    }
}