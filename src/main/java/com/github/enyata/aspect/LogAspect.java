package com.github.enyata.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;


@Aspect
@Component
public class LogAspect {

    public static Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Around("@annotation(com.github.enyata.aspect.Log)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] obj = joinPoint.getArgs();
        Method method = signature.getMethod();

        String signatureArgs = "";


        if (method.isAnnotationPresent(Log.class)) {
            String[] parametersName = signature.getParameterNames();

            int idx = Arrays.asList(parametersName).indexOf("input");

            if(obj.length > idx) {
                signatureArgs = (String) obj[idx];
            }

            LOGGER.info("Method name: {}(), Data to Encrypt: {} ", method.getName(), signatureArgs );
        }

        try {
            String output = (String) joinPoint.proceed();
            LOGGER.info("Method name: {}(), Data to value: {},  output: {} ", method.getName(), signatureArgs, output );
            return output;
        } catch (Throwable throwable) {
            throw throwable;
        }
    }
}