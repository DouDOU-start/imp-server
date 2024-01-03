package cn.hanglok.algoSched.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Allen
 * @version 1.0
 * @className LoggingAspect
 * @description TODO
 * @date 2024/1/3
 */

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(*  cn.hanglok.algoSched.controller.*.*(..))")
    public void controllerLayer() {
    }

    @Around("controllerLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String reqId = UUID.randomUUID().toString();

        long startTime = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = joinPoint.getTarget().getClass().getName();
        Object[] array = joinPoint.getArgs();

        log.info("\n" + "===> ReqId: " + reqId +
                "\n" + "===> TargetClass: " + className +
                "\n" + "===> TargetMethod: " + methodName +
                "\n" + "===> Args: " +  Arrays.toString(array));

        Object result = joinPoint.proceed();

        long timeTaken = System.currentTimeMillis() - startTime;

        log.info("\n" + "<=== ReqId: " + reqId +
                "\n" + "<=== Result: " + result +
                "\n" + "<=== ExecuteTime: " + timeTaken + " ms");

        return result;
    }

}
