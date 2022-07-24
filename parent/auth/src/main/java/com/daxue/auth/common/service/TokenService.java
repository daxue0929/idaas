package com.daxue.auth.common.service;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Service;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;


/**
 * @author daxue0929
 * @date 2022/07/23
 **/
@Service
public class TokenService {

    public String generateRSAToken(Map<String, Object> payload) throws JOSEException {
//        Set<Map.Entry<String, Object>> entries = payload.entrySet();
//        for (Map.Entry<String, Object> entry : entries) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//        }
        PrivateKey privateKey = getDefaultRSAPrivateKey();
        RSAPublicKey publicKey = getRSAPublicKey();
        RSAKey rsaJWK = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWSSigner signer = new RSASSASigner(rsaJWK);
        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                new Payload(payload));
        jwsObject.sign(signer);
        String token = jwsObject.serialize();
        return token;
    }

    public RSAPublicKey getRSAPublicKey() {
        try {
            String filePath = this.getClass().getClassLoader().getResource("public_key.pem").getFile();

            InputStreamReader inputStream = new InputStreamReader(new FileInputStream(filePath)); //获取一个输入流


            File publicKeyFile = new File(filePath);
            byte[] keyBytes = new byte[(int) publicKeyFile.length()];
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * java.security.spec.InvalidKeySpecException: java.security.InvalidKeyException: invalid key format
     * 解释：https://www.cnblogs.com/jpfss/p/10206054.html
     * @return
     * openssl pkcs8 -topk8 -inform PEM -in private_key.pem -outform pem -nocrypt -out pkcs8.pem
     */
    public PrivateKey getDefaultRSAPrivateKey() {
        try {
            String filePath = this.getClass().getClassLoader().getResource("pkcs8.pem").getFile();
            File privateKeyFile = new File(filePath);
            byte[] keyBytes = new byte[(int) privateKeyFile.length()];
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
