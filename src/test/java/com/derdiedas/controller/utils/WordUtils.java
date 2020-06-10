package com.derdiedas.controller.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derdiedas.dto.LearningWordDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@Component
public class WordUtils {

  private final HttpHeadersUtils httpHeadersUtils;
  private final ApiDocsUtils apiDocsUtils;
  private final ObjectMapper objectMapper;

  @Autowired
  public WordUtils(HttpHeadersUtils httpHeadersUtils,
                   ApiDocsUtils apiDocsUtils,
                   ObjectMapper objectMapper) {
    this.httpHeadersUtils = httpHeadersUtils;
    this.apiDocsUtils = apiDocsUtils;
    this.objectMapper = objectMapper;
  }

  public void studyWord(MockMvc mockMvc, long learningWordId, String apiDocsId, String authenticationKey) throws Exception {

    HttpHeaders httpHeaders = httpHeadersUtils.addAuthTokenToHttpHeaders(authenticationKey);

    ResultActions resultActions = mockMvc
        .perform(put("/learningWords/" + learningWordId)
            .headers(httpHeaders)
            .param("isStudied", Boolean.TRUE.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk());

    MvcResult mvcResult = apiDocsUtils.appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);

    String contentAsString = mvcResult.getResponse().getContentAsString();
    LearningWordDto result = objectMapper.readValue(contentAsString, LearningWordDto.class);

    assertNotNull(result);
    assertTrue(result.isStudied());
  }

  public void studyWord(MockMvc mockMvc,long learningWordId) throws Exception {
    studyWord(mockMvc, learningWordId, null, null);
  }

  public void studyWord(MockMvc mockMvc, long learningWordId, String apiDocsId) throws Exception {
    studyWord(mockMvc, learningWordId, apiDocsId, null);
  }

  public void studyWordWithAuthKey(MockMvc mockMvc,long learningWordId, String authenticationKey) throws Exception {
    studyWord(mockMvc, learningWordId, null, authenticationKey);
  }
}
