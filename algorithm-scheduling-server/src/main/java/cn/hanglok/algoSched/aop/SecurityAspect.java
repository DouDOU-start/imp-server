package cn.hanglok.algoSched.aop;

import cn.hanglok.algoSched.exception.InvalidTokenException;
import cn.hanglok.algoSched.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Allen
 * @version 1.0
 * @className VerifyAspect
 * @description TODO
 * @date 2024/1/28
 */
@Aspect
@Component
@Slf4j
public class SecurityAspect {

    @Autowired
    private TokenService tokenService;

    @Around("@annotation(cn.hanglok.algoSched.annotation.RequireValidToken)")
    public Object validateTokenAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取HttpServletRequest，从中获取请求头Token
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Token");

        if (tokenService.validateToken(token)) {
            return joinPoint.proceed();
        }

        throw new InvalidTokenException("Invalid or missing Token.");
    }
}
