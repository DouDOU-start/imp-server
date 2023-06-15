package cn.hanglok.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

/**
 *  @className MyAspect
 *  @description TODO
 *  @author Allen
 *  @date 2023/5/22 10:48
 *  @version 1.0
 */
@Aspect
@Component
public class MyAspect {

    @Before("execution (* com.example.demo.controller.*.*(..)) && args(.., request)")
    public void captureRequestHeaders(JoinPoint joinPoint, HttpServletRequest request) {
//        String userAgent = request.getHeader("User-Agent");
//        String authorizationHeader = request.getHeader("Authorization");
//
//        System.out.println("User-Agent: " + userAgent);
//        System.out.println("Authorization Header: " + authorizationHeader);

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            System.out.printf("%s：%s%n", element, request.getHeader(element));
        }
    }

    @Before("execution (* com.example.demo.controller.*.*(..)) && args(.., request)")
    public void getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        System.out.println("Client IP: " + ipAddress);
    }

}
