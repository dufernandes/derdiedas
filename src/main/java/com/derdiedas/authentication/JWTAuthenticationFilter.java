package com.derdiedas.authentication;

import com.auth0.jwt.JWT;
import com.derdiedas.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.derdiedas.authentication.SecurityConstants.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        Credentials credentials = null;
        try {
            credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), Credentials.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (credentials == null) {
            throw new AuthenticationServiceException("The entered data could not be parsed in credentials");
        }

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword(),
                        new ArrayList<>()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
