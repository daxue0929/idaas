package com.example.securitytest.config.filter;

import com.example.securitytest.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过滤器
 * @author daxue0929
 * @date 2022/7/31
 */

public class TokenLoginfilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;

    public TokenLoginfilter (AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/login", "POST"));
    }




    // 1. 获取表单提交过来的信息，比如用户名，密码。
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        return super.attemptAuthentication(request, response);
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // 2. 认证成功调用的方法

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);


    }



    // 3. 认证失败调用的方法


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        super.unsuccessfulAuthentication(request, response, failed);



    }
}
