package com.example.securitytest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * http调用未授权清情况下的返回，统一的处理类。
 * @author daxue0929
 * @date 2022/7/31
 */
@Slf4j
public class UnauthEntrypoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.info("未授权的情况下，封装一个response返回。");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            PrintWriter out = response.getWriter();
            out.append("未授权");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
