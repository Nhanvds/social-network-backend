package com.project.socialnetwork.components;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.project.socialnetwork.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.StringJoiner;

@Component
public class JwtCreate {
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secretKey}")
    public String secretKey;

    public String generateToken(User user) throws JOSEException {
        String email = user.getEmail();
        Long userId = user.getId();
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        StringJoiner roles = new StringJoiner(" ");
        user.getRoles().forEach(role -> roles.add(role.getRoleName()));
        JWTClaimsSet jwtClaimsSet =new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("issuer_vdn")
                .issueTime(new Date())
                .expirationTime(new Date(
                        System.currentTimeMillis()+expiration
                ))
                .claim("user_id",userId)
                .claim("scope",roles.toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header,payload);
        jwsObject.sign(new MACSigner(secretKey.getBytes()));
        return jwsObject.serialize();
    }
}
