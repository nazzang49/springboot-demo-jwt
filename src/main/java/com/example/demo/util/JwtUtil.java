package com.example.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.property.JwtProperties;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public abstract class JwtUtil {

    public static String createToken(UserDetails userDetails) {
        return createToken(userDetails, DateUtil.nowAfterDaysToDate(JwtProperties.EXPIRES_LIMIT));
    }

    private static String createToken(UserDetails userDetails, Date date) {
        try {
            return JWT.create()
                    .withIssuer(JwtProperties.ISSUER)
                    .withClaim("id", userDetails.getUsername())
                    .withClaim("role", userDetails.getAuthorities().toArray()[0].toString())
                    .withExpiresAt(date)
                    .sign(JwtProperties.getAlgorithm());
        } catch (JWTCreationException createEx) {
            return null;
        }
    }

    public static Boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(JwtProperties.getAlgorithm()).build();
            verifier.verify(token);

            return Boolean.TRUE;
        } catch (JWTVerificationException verifyEx) {
            return Boolean.FALSE;
        }
    }

    public static String refreshToken(UserDetails userDetails) {
        return createToken(userDetails, DateUtil.nowAfterDaysToDate(JwtProperties.EXPIRES_LIMIT));
    }

    public static DecodedJWT tokenToJwt(String token) {
        try {
            return JWT.decode(token);
        } catch (JWTDecodeException decodeEx) {
            return null;
        }
    }
}