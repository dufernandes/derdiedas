package com.derdiedas.authentication;

import com.auth0.jwt.JWT;
import com.derdiedas.model.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.derdiedas.authentication.SecurityConstants.SECRET;
import static com.derdiedas.authentication.SecurityConstants.TOKEN_PREFIX;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

class JwtUtils {

    static String createJWTToken(User user) {
        return user != null && isNotEmpty(user.getUsername())
                ? JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()))
                : null;
    }


    static String extractUserPrincipalFromJwtToken(String token) {
        return isNotEmpty(token) ? JWT.require(HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject() : null;
    }
}
