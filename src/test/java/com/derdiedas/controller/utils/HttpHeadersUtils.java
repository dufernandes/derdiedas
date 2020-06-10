package com.derdiedas.controller.utils;

import static com.derdiedas.authentication.SecurityConstants.HEADER_STRING_AUTHORIZATION;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HttpHeadersUtils {

  public HttpHeaders addAuthTokenToHttpHeaders(String authenticationKey) {
    HttpHeaders httpHeaders = new HttpHeaders();
    if (isNotEmpty(authenticationKey)) {
      httpHeaders.add(HEADER_STRING_AUTHORIZATION, authenticationKey);
    }
    return httpHeaders;
  }
}
