package com.derdiedas.controller;

import com.derdiedas.controller.dto.BaseUserITCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationITCase extends BaseUserITCase {

    @Test
    void authenticateUser_validCredentials_userAuthenticatedSuccessfully() throws Exception {
        createUser("email@email.com", "password", "first name", "last name");
        authenticateUser();
    }

    @Test
    void authenticateUser_invalidCredentials_userAuthenticatedUnsuccessfully() throws Exception {
        createUser("email@email.com", "password", "first name", "last name");
        authenticateUserWithWrongCredentials();
    }

    @Test
    void fetchUsers_notLoggedIn_returnForbidden() throws Exception {
        this.mockMvc
                .perform(get("/users").param("email", "email@email.com0"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andDo(document("user",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn();
    }

    private void authenticateUser() throws Exception {
        String body = "{\n" +
                "    \"username\": \"email@email.com\",\n" +
                "    \"password\": \"password\"\n" +
                "}";

        this.mockMvc
                .perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print()).andExpect(status().isOk())
                .andDo(document("login",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn();
    }

    private void authenticateUserWithWrongCredentials() throws Exception {
        String body = "{\n" +
                "    \"username\": \"email@email.com\",\n" +
                "    \"password\": \"passwordless\"\n" +
                "}";

        this.mockMvc
                .perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print()).andExpect(status().isUnauthorized())
                .andDo(document("login",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn();
    }
}
