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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Component
public class UserAuthenticationUtils {

  public String authenticateUser(MockMvc mockMvc, String userName, String password) throws Exception {

    Credentials credentials = new Credentials();
    credentials.setUsername(userName);
    credentials.setPassword(password);
    ObjectMapper jsonTransformer = new ObjectMapper();
    String requestBodyAsJsonString = jsonTransformer.writeValueAsString(credentials);

    MvcResult mvcResult = mockMvc
        .perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBodyAsJsonString))
        .andDo(print()).andExpect(status().isOk())
        .andDo(MockMvcRestDocumentation.document(SpringRestDocs.LoginPage.LOGIN_SUCCESSFUL,
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
        .andReturn();

    return mvcResult.getResponse().getHeader(HEADER_STRING_AUTHORIZATION);
  }

}
