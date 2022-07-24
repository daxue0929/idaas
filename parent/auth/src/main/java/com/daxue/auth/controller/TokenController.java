package com.daxue.auth.controller;

import com.daxue.auth.annotation.PassToken;
import com.daxue.auth.common.service.TokenService;
import com.nimbusds.jose.JOSEException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daxue0929
 * @date 2022/07/23
 **/

@RestController
public class TokenController extends BaseController{

    final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 获取token接口
     * @param tokenUser
     * @return
     * @throws JOSEException
     */
    @PassToken
    @PostMapping("/get_token")
    public Map<String,Object> getToken(@RequestBody TokenUser tokenUser) throws JOSEException {
        HashMap<String, Object> resultMap = new HashMap<>();
        String username = tokenUser.getUsername();
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        resultMap.put("access_token", tokenService.generateRSAToken(map));
        return sendBaseNormalMap(resultMap);
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TokenUser {
    public String username;
    public String password;
    public String tokenType;
}
