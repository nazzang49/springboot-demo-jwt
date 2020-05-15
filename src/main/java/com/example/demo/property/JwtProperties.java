package com.example.demo.property;

import com.auth0.jwt.algorithms.Algorithm;

public class JwtProperties {

    public static final String SECRET = "JYP";
    public static final long EXPIRES_LIMIT = 3L;
    public static final String HEADER_NAME = "jwt-header";
    public static final String ISSUER = "JYP";

    public static Algorithm getAlgorithm() {
        try {
            return Algorithm.HMAC256(JwtProperties.SECRET);
        } catch (IllegalArgumentException e) {
            return Algorithm.none();
        }
    }
}
