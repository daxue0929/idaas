package com.daxue.auth.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.daxue.auth.annotation.PassToken;
import com.daxue.auth.utils.StatusCode;
import com.sun.deploy.net.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;


/**
 * @author daxue0929
 * @date 2022/07/23
 **/

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //不是映射到方法，直接通过
        if (!(handler instanceof HandlerMethod)) {
            log.info("Passed Without Check The Token");
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                log.info("Passed Needn't Check The Token");
                return true;
            }
        }
        String token = getToken(request);
        if (StringUtils.isBlank(token)) {
            return result(response, "token 不存在");
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


    public boolean result(HttpServletResponse httpServletResponse, String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("return_code", StatusCode.SESSION_EXPIRE);
        jsonObject.put("return_msg", msg);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try {
            PrintWriter out = httpServletResponse.getWriter();
            out.append(jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getToken(HttpServletRequest request) {
        String token = null;
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            token = authorization;
        }

        Cookie[] cookies = request.getCookies();
        if (token == null && cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        String access_token = request.getParameter("access_token");
        if (token == null && access_token != null) {
            token = access_token;
        }
        return token;
    }
}
