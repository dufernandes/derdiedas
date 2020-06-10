package com.derdiedas.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derdiedas.controller.helper.UserHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class AuthenticationITCase extends BaseITCase {

  @Autowired
  private UserHelper userHelper;

  @Test
  void authenticateUser_whenCredentialsAreValid_thenUserAuthenticatedIsSuccessfully() throws Exception {
    userHelper.createUser(getMockMvc(),"email@email.com", "password", "first name", "last name", "users/create-user");
    authenticateUser();
  }

  @Test
  void authenticateUser_whenCredentialsAreInvalid_thenUserAuthenticationIsUnsuccessful() throws Exception {
    userHelper.createUser(getMockMvc(),"email@email.com", "password", "first name", "last name", "users/create-user");
    authenticateUserWithWrongCredentials();
  }

  @Test
  void fetchUsers_whenUserIsNotLoggedIn_ThenReturnForbidden() throws Exception {
    getMockMvc()
        .perform(get("/users").param("email", "email@email.com0"))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andReturn();
  }

  private void authenticateUser() throws Exception {
    String body = "{\n" +
        "    \"username\": \"email@email.com\",\n" +
        "    \"password\": \"password\"\n" +
        "}";

    getMockMvc()
        .perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print()).andExpect(status().isOk())
        .andDo(document(SpringRestDocs.LoginPage.LOGIN_SUCCESSFUL,
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
        .andReturn();
  }

  private void authenticateUserWithWrongCredentials() throws Exception {
    String body = "{\n" +
        "    \"username\": \"email@email.com\",\n" +
        "    \"password\": \"passwordless\"\n" +
        "}";

    getMockMvc()
        .perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print()).andExpect(status().isUnauthorized())
        .andDo(document(SpringRestDocs.LoginPage.LOGIN_FAILED,
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
        .andReturn();
  }
}
