package com.derdiedas.authentication;

import com.derdiedas.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.derdiedas.authentication.SecurityConstants.HEADER_STRING;
import static com.derdiedas.authentication.SecurityConstants.TOKEN_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class JWTAuthenticationFilterTest {

    private static final String PRINCIPAL = "principal";
    private static final String CREDENTIALS = "credentials";
    private static final String USER_NAME = "userName";

    @InjectMocks
    private JWTAuthenticationFilter filter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        filter = new JWTAuthenticationFilter(authenticationManager);
    }

    @Test
    void attemptAuthentication_validAuthenticationParameters_returnValidAuthentication() throws IOException {

        ServletInputStream credentialsInputStream = createCredentialsIputStream();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getInputStream()).thenReturn(credentialsInputStream);

        Authentication result = filter.attemptAuthentication(request, null);

        assertNotNull(result);
        assertEquals(PRINCIPAL, result.getPrincipal());
        assertEquals(CREDENTIALS, result.getCredentials());
    }

    @Test
    void successfulAuthentication_validParameters_successfulTokenCreation() {
        User principal = mock(User.class);
        when(principal.getUsername()).thenReturn(USER_NAME);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);

        String token = filter.createJWTToken(principal);

        HttpServletResponse response = mock(HttpServletResponse.class);
        doNothing().when(response).addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        filter.successfulAuthentication(null, response, null, authentication);
        verify(response).addHeader(HEADER_STRING, TOKEN_PREFIX + filter.createJWTToken(principal));
    }

    private ServletInputStream createCredentialsIputStream() {
        String credentialsString = "{\n" +
                "    \"username\": \"email@email.com\",\n" +
                "    \"password\": \"password\"\n" +
                "}";

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(credentialsString.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    private AuthenticationManager authenticationManager = new AuthenticationManager() {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return new UsernamePasswordAuthenticationToken(PRINCIPAL, CREDENTIALS);
        }
    };
}