package com.derdiedas.authentication;

public class SecurityConstants {

    static final String SECRET = "SecretKeyToGenJWTs";
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING_AUTHORIZATION = "Authorization";
    public static final String HEADER_USER_ID = "userId";
    static final String SIGN_UP_URL_PATTERN = "/users/**";
    static final String STATIC_DOCS_URL_PATTERN = "/docs/**";
    static final String ROOT_URL_PATTERN = "/";
}
