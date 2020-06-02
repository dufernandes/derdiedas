package com.derdiedas.authentication;

import com.derdiedas.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.derdiedas.authentication.SecurityConstants.HEADER_STRING_AUTHORIZATION;
import static com.derdiedas.authentication.SecurityConstants.TOKEN_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTAuthorizationFilterTest extends JwtAuthenticationBase {

    @Spy
    @InjectMocks
    private JWTAuthorizationFilter filter = new JWTAuthorizationFilter(authenticationManager);

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void doFilterInternal_validParameters_noExceptionsThrown() throws IOException, ServletException {
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(any(), any());

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HEADER_STRING_AUTHORIZATION)).thenReturn(TOKEN_PREFIX + " abc");

        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);

        doReturn(authenticationToken).when(filter).getAuthentication(any());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authenticationToken);
        SecurityContextHolder.setContext(securityContext);

        filter.doFilterInternal(request, null, filterChain);

        verify(filter).getAuthentication(any());
        verify(filterChain).doFilter(any(), any());
    }

    @Test
    void getAuthentication_validParameters_returnToken() {
        User user = User.builder().email("email@email.com").build();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HEADER_STRING_AUTHORIZATION)).thenReturn(TOKEN_PREFIX + JwtUtils.createJWTToken(user));

        String userTokenString = JwtUtils.extractUserPrincipalFromJwtToken(request.getHeader(
            HEADER_STRING_AUTHORIZATION));

        UsernamePasswordAuthenticationToken userToken = filter.getAuthentication(request);
        assertNotNull(userToken);
        assertEquals(userTokenString, userToken.getPrincipal());
    }
}