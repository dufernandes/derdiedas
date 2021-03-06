package com.derdiedas.authentication;

import com.derdiedas.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

import static com.derdiedas.authentication.SecurityConstants.HEADER_STRING_AUTHORIZATION;
import static com.derdiedas.authentication.SecurityConstants.HEADER_USER_ID;
import static com.derdiedas.authentication.SecurityConstants.TOKEN_PREFIX;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        Credentials credentials = null;
        try {
            credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), Credentials.class);
        } catch (IOException ioe) {
            log.error("Error while reading credentials to authenticate user", ioe);
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

        User user = (User) auth.getPrincipal();
        String token = JwtUtils.createJWTToken(user);
        res.addHeader(HEADER_STRING_AUTHORIZATION, TOKEN_PREFIX + token);
        res.addHeader(HEADER_USER_ID, Long.toString(user.getId()));
    }

}
