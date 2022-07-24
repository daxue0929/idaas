package com.daxue.auth.common.service;


import com.google.common.io.BaseEncoding;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
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
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;


/**
 * @author daxue0929
 * @date 2022/07/23
 **/
@Service
public class TokenService {


    String publicKeyString = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz5SepSkXjX8wAlQM25QM\n" +
            "/pJd11wc+jC8UhSHp0skpO5jPxDtQgkVjWi8G8E1Jgw8LxqxLbduS/FqXND1HRyk\n" +
            "LF0tJSvGN6KOUKRlZQSqEpcieT3t+t89iaf5+RYS7OgSglETGWXa0uShCaLNR2r1\n" +
            "pqjTozy2ffXxdGEoguu+zxnYjVa+ru905LB2Cc0EbC3sRUSQKmt6tUv7yMghwkeO\n" +
            "6rAjJncRXU1zAykeyQjJzbJbc7PPg/VDjDtJqHp6fspRcTK3JjAhZkdHKbBr7zwK\n" +
            "rFmOcDBugTLceQYzr+e6Bex6Yok0MnGp48IOPpH9OtBB/Bo/yBm4bli0SGVt1iua\n" +
            "ZwIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";
    // pkcs8 string
    String privateKeyString = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDPlJ6lKReNfzAC\n" +
            "VAzblAz+kl3XXBz6MLxSFIenSySk7mM/EO1CCRWNaLwbwTUmDDwvGrEtt25L8Wpc\n" +
            "0PUdHKQsXS0lK8Y3oo5QpGVlBKoSlyJ5Pe363z2Jp/n5FhLs6BKCURMZZdrS5KEJ\n" +
            "os1HavWmqNOjPLZ99fF0YSiC677PGdiNVr6u73TksHYJzQRsLexFRJAqa3q1S/vI\n" +
            "yCHCR47qsCMmdxFdTXMDKR7JCMnNsltzs8+D9UOMO0moenp+ylFxMrcmMCFmR0cp\n" +
            "sGvvPAqsWY5wMG6BMtx5BjOv57oF7HpiiTQycanjwg4+kf060EH8Gj/IGbhuWLRI\n" +
            "ZW3WK5pnAgMBAAECggEAFckFNVQwsprskPH9P3Y0Puthid6S8/b1bK/W5MnQGLck\n" +
            "F1RRciK97k9VnpwSVeHm1xK4EZI0syes//y2HM4KtNU6jbIgZWpcAT6fnZB1wOm3\n" +
            "pn3HYhLXxICqjWexqMdaWe7kUEAOTR/NftNlM9+X1FvXikQu6t1K7+nadJMFPXrK\n" +
            "T2QS/oEsXgO1m+8mY0AxBOqOTH1v3BWocUPXkytyf9xgo+DLGSPuXAlf3pETmmhn\n" +
            "QvScSx5m+IqadU8cqBhVbKDD66CtHBTfYTZD/4qbVLANMF+w8l3JUyLnJSJz/Ntd\n" +
            "eApQtNRrKc2e2OeXz3n0Q0Hc9EdNItmJsTGOm2hgcQKBgQD+JKA7UzD1OelBpc+v\n" +
            "E84LiUpfydN9+35kBW7Lo62ASK/pVEh9HW0y/Gn82QFt9IR3qJVDqTY+ZdJkX4jg\n" +
            "wLYSOztzjgEQZQ5BRRS0Ynw8t7CFQUGRuEnIBEubl5/OLQDE6enbXdHAtLmWkEuA\n" +
            "sCZbntgpDNLWIaZSclX/vyu/SwKBgQDRGOYQ7BPZZdf3vCxLDrtHyvSimqq7rs3L\n" +
            "6U2LKGPNcuwIrlKlcpsdHXX2GDD27TQUpmzPmuzMKwp33G0GFSMnIXzdJmq9dGHp\n" +
            "J+pa9Ay+RmsuPR5OYOPhbhhg29m1O+A7ondFNc0+yCCi6TczGpC/r0lgRfMWB6pl\n" +
            "mvuP2Yyz1QKBgQD2aR6uxqeGbUL7csBeeW5jAxeIrpCP6yON6TKW4blc0500N5Md\n" +
            "NEXnlJNTJDjdmqJBvsQS2WMNtv36ciKuAEvIe4PWM45WjhT3NfqwN3lMCbJuUWo4\n" +
            "1p07AOcqEiZQ6pN/WWl0V5ADL3duQ7PhL2nRlb7ydkuKcQ/pipBVqs7I5wKBgDRx\n" +
            "TzqNS0kqLul2a5GDp1sRPxwNOKcL55et8wHk5msf2K0ws4/FV7wMqIpJdPZpdbd8\n" +
            "KixDigKHBQAc68crw/5LetqoiPrtVqU4mqGnNg7+8GDPeWtlB6KBj2Gzoj2/P7UV\n" +
            "QV69aLXHUdAqN+r8TbOVzgx0Xsx2rbkADvn8MTrlAoGBAN8dDS4eWHIqb1w9ZFGp\n" +
            "NXR6eYzuNXY/Evy/N0fupFtwPCQ80x6b26WdC3g1TgypPMRtrKjANo+EEEjB7ZEn\n" +
            "fOZJklT+jFrGAyaXZBQ4swduFOW13AHBBInMyD9GtlN8o7bY1JJQ9Iy7uC/XR3UU\n" +
            "lqgYHZSaroGP7fVdjSjq0NNF\n" +
            "-----END PRIVATE KEY-----\n";

    public RSAKey rsaJWK;

    TokenService() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PrivateKey privateKey = getPrivateKey();
        RSAPublicKey publicKey = getPublicKey();
        rsaJWK = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    }


    public String generateRSAToken(Map<String, Object> payload) {
        try {
            JWSSigner signer = new RSASSASigner(rsaJWK);
            JWSObject jwsObject = new JWSObject(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                    new Payload(payload));
            jwsObject.sign(signer);
            String token = jwsObject.serialize();
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKey = privateKeyString.replaceAll(
                "-----(.*)-----(\r\n?|\n|)([\\s\\S]*)(\r\n?|\n|)-----(.*)-----",
                "$3"
        ).replaceAll("\n", "").replaceAll(" ", "");
        byte[] clear = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey aPrivate = factory.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return aPrivate;
    }

    public RSAPublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKey = publicKeyString.replaceAll(
                "-----(.*)-----(\r\n?|\n|)([\\s\\S]*)(\r\n?|\n|)-----(.*)-----",
                "$3"
        ).replaceAll("\n", "").replaceAll(" ", "");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(BaseEncoding.base64().decode(publicKey));
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        return rsaPublicKey;
    }

    public String getUsername(String token) {
        try {
            if (verifyPublic(token)) {
                SignedJWT signedJWT  = SignedJWT.parse(token);
                JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
                String username = String.valueOf(jwtClaimsSet.getClaim("username"));
                return username;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verifyPublic(String token) {
        try {
            SignedJWT signedJWT  = SignedJWT.parse(token);
            RSASSAVerifier verifier = new RSASSAVerifier(rsaJWK.toPublicJWK());
            return signedJWT.verify(verifier);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return false;
    }
}


