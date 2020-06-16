package com.derdiedas.controller.utils;

import static com.derdiedas.authentication.SecurityConstants.HEADER_STRING_AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derdiedas.authentication.Credentials;
import com.derdiedas.controller.SpringRestDocs;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@Component
public class UserAuthenticationUtils {

  private final ApiDocsUtils apiDocsUtils;

  @Autowired
  public UserAuthenticationUtils(ApiDocsUtils apiDocsUtils) {
    this.apiDocsUtils = apiDocsUtils;
  }

  public String authenticateUser(MockMvc mockMvc, String userName, String password, String apiDocsId) throws Exception {

    Credentials credentials = new Credentials();
    credentials.setUsername(userName);
    credentials.setPassword(password);
    ObjectMapper jsonTransformer = new ObjectMapper();
    String requestBodyAsJsonString = jsonTransformer.writeValueAsString(credentials);

    ResultActions resultActions = mockMvc
        .perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBodyAsJsonString))
        .andDo(print()).andExpect(status().isOk());

    MvcResult mvcResult = apiDocsUtils.appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);

    return mvcResult.getResponse().getHeader(HEADER_STRING_AUTHORIZATION);
  }

  public String authenticateUser(MockMvc mockMvc, String userName, String password) throws Exception {
    return authenticateUser(mockMvc, userName, password, null);
  }

}
