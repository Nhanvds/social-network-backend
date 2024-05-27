package com.project.socialnetwork.components;

import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.socialnetwork.enums.ErrorCode;
import com.project.socialnetwork.exception.ParserTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.secretKey}")
    private String secretKey;

    public SignedJWT decodedJWT(String token) throws ParserTokenException {
        try {
            token = token.substring(7);
            return SignedJWT.parse(token);
        } catch (ParseException ex) {
            throw new ParserTokenException();
        }

    }

    public Long getUserId(String token) throws ParserTokenException {
        try {
            SignedJWT decodedJWT = decodedJWT(token);
            JWTClaimsSet jwtClaimsSet = decodedJWT.getJWTClaimsSet();
            Long userId = jwtClaimsSet.getLongClaim("user_id");
            return userId;
        } catch (ParseException ex) {
            throw new ParserTokenException();
        }
    }

    public String getEmail(String token) throws ParserTokenException {
        try {
            SignedJWT decodedJWT = decodedJWT(token);
            String email = decodedJWT.getJWTClaimsSet().getSubject();
            return email;
        } catch (ParseException ex) {
            throw new ParserTokenException();
        }

    }

}
