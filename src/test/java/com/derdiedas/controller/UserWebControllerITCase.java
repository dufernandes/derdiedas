package com.derdiedas.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.derdiedas.controller.utils.ApiDocsUtils;
import com.derdiedas.controller.utils.AssertionUtils;
import com.derdiedas.controller.utils.HttpHeadersUtils;
import com.derdiedas.controller.utils.UserUtils;
import com.derdiedas.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Tests here only validate the Rest API setup and request. The business logic is mocked
 * and tested in other integration tests, or unit tests.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import({UserUtils.class, ApiDocsUtils.class, HttpHeadersUtils.class, AssertionUtils.class})
@AutoConfigureRestDocs
class UserWebControllerITCase {

  private static final String EMAIL = "email@email.com0";
  private static final String PASSWORD = "password";
  private static final String FIRST_NAME = "first name0";
  private static final String LAST_NAME = "last name0";

  @MockBean
  private UserService userService;

  @Autowired
  private UserUtils userUtils;

  @Autowired
  private AssertionUtils assertionUtils;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    when(userService.createUser(any())).thenReturn(null);
  }

  @Test
  void create_whenDataIsValid_thenReturnCreatedUser() throws Exception {
    MvcResult result =
        userUtils.createUser(mockMvc, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME,
            SpringRestDocs.UsersPage.CREATE_USER);
    assertionUtils.assertHttpResponseIsOk(result);
  }

  @Test
  void create_whenFirstNameIsMissing_thenError() throws Exception {
    MvcResult result =
        userUtils.createUser(mockMvc, EMAIL, PASSWORD, null, LAST_NAME,
            SpringRestDocs.UsersPage.CREATE_USER_FAILURE);
    assertionUtils.assertHttpResponseIsBadRequest(result);
  }
}