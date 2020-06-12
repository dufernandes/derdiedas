package com.derdiedas.controller.utils;

import static org.junit.jupiter.api.Assertions.fail;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AssertionUtils {

  public boolean assertHttpResponseIsOk(MvcResult mvcResult) {
    boolean isHttpStatusOk = mvcResult.getResponse().getStatus() == HttpStatus.OK.value();
    if (!isHttpStatusOk) {
      log.warn("If there is an exception, it will be shown next", mvcResult.getResolvedException());
      fail("There was a problem executing the Http request, please check the logs.");
    }
    return isHttpStatusOk;
  }

  public boolean assertHttpResponseIsBadRequest(MvcResult mvcResult) {
    boolean isHttpBadRequest;
    if (isHttpBadRequest = mvcResult.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value()) {
      log.warn("If there is an exception, it will be shown next", mvcResult.getResolvedException());
    } else {
      fail("Http Response was not 400.");
    }
    return isHttpBadRequest;
  }
}
