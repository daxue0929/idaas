package com.daxue.auth.controller;

import com.daxue.auth.annotation.PassToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;
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


    @PassToken
    @PostMapping("/get_token")
    public Map<String,Object> getToken(@RequestBody TokenUser tokenUser) {
        HashMap<String, Object> resultMap = new HashMap<>();
        String username = tokenUser.getUsername();
        /**
         * todo
         * 1. 支持生成两种类型的token，RSA非对称，
         * RS对称。
         */
        resultMap.put("access_token", Base64.encodeBase64String(username.getBytes()));
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
