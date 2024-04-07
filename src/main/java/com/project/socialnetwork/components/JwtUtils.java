package com.project.socialnetwork.components;

import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.secretKey}")
    private String secretKey ;
    public SignedJWT decodedJWT(String token) throws ParseException {
        return SignedJWT.parse(token);
    }
    public Long getUserId(String token) throws ParseException {
        SignedJWT decodedJWT = decodedJWT(token);
        JWTClaimsSet jwtClaimsSet = decodedJWT.getJWTClaimsSet();
        Long userId = jwtClaimsSet.getLongClaim("user_id");
        return userId;
    }
    public String getEmail(String token) throws ParseException {
        SignedJWT decodedJWT = decodedJWT(token);
        String email = decodedJWT.getJWTClaimsSet().getSubject();
        return email;
    }

}
