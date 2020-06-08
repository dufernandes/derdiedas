package com.derdiedas.authentication;

import com.derdiedas.model.User;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    @Test
    void createJWTToken_whenUserIsValid_thenReturnToken() {
        User user = User.builder().email("email@email.com").build();
        String result = JwtUtils.createJWTToken(user);
        assertNotNull(result);
    }

    @Test
    void createJWTToken_whenUserIsNull_thenReturnNull() {
        assertNull(JwtUtils.createJWTToken(null));
    }

    @Test
    void createJWTToken_whenEmailIsNull_thenReturnNull() {
        assertNull(JwtUtils.
                createJWTToken(User.builder().email(null).build()));
    }

    @Test
    void extractUserFromJwtToken_whenTokenIsValid_thenReturnUserPrincipal() {
        User user = User.builder().email("email@email.com").build();
        String token = JwtUtils.createJWTToken(user);
        String result = JwtUtils.extractUserPrincipalFromJwtToken(token);
        assertNotNull(result);
        assertEquals("email@email.com", result);
    }

    @Test
    void extractUserFromJwtToken_whenTokenIsNull_thenReturnNull() {
        assertNull(JwtUtils.extractUserPrincipalFromJwtToken(null));
    }

    @Test
    void extractUserFromJwtToken_whenTokenIsEmpty_thenReturnNull() {
        assertNull(JwtUtils.extractUserPrincipalFromJwtToken(StringUtils.EMPTY));
    }
}