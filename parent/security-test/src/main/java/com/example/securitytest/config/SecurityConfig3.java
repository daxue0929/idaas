package com.example.securitytest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author daxue0929
 * @date 2022/7/31
 */

@Slf4j
public class SecurityConfig3 {



}


@Slf4j
class DefaultPasswordEncoder implements PasswordEncoder {


    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    /**
     * 进行密码的一个比对
     * @param rawPassword //未加密的原始密码
     * @param encodedPassword //加密过的密码
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword.toString().equals(encodedPassword)) {
            return true;
        }
        return false;
    }
}

@Slf4j
class TokenLogouthandler implements LogoutHandler{

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        /**
         * 1.从request header中获取token，比对一下删除指定的token
         *
         * 2.从redis中移除token。

         */

        //

        log.info("从redis中移除token了");
    }
}


class TokenService {

    public String createToken() {
        return "token-daxue";
    }

    public String getUserInfo() {
        return "daxue";
    }
}