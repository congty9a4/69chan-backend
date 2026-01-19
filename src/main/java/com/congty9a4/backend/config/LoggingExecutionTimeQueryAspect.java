package com.congty9a4.backend.config;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
//Replate <> with your package

//Source: https://gist.github.com/neo7BF/e2569434a6d40b790c5f837a2ee67723

@Component
@Aspect
@Slf4j
public class LoggingExecutionTimeQueryAspect {
    @Around("@annotation(com.congty9a4.backend.annotation.TrackExecutionTime)")
    public Object trackTime (ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj = pjp.proceed();
        long endTime = System.currentTimeMillis();
        log.info("Execution time: " + DurationFormatUtils.formatDurationHMS(endTime-startTime) + "\nMethod: " + pjp.getSignature());
        return obj;
    }
}

