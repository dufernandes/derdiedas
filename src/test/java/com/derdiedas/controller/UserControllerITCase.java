package com.derdiedas.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.derdiedas.bootstrap.importer.ImporterFromFirstList;
import com.derdiedas.controller.utils.UserUtils;
import com.derdiedas.dto.UserDto;
import com.derdiedas.repository.LearningWordRepository;
import com.derdiedas.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;

import javax.servlet.ServletContext;

class UserControllerITCase extends BaseITCase {

  private static final String EMAIL = "email@email.com0";
  private static final String PASSWORD = "password";
  private static final String FIRST_NAME = "first name0";
  private static final String LAST_NAME = "last name0";

  private static final String DIE = "die";
  private static final String ZEIT = "Zeit";
  private static final String DAS = "das";
  private static final String ZIMMER = "Zimmer";

  private static final String TUR = "Tür";
  private static final String DER = "der";
  private static final String RUCKEN = "Rücken";

  @Autowired
  private ImporterFromFirstList importer;

  @Autowired
  private WordRepository wordRepository;

  @Autowired
  private LearningWordRepository learningWordRepository;

  @Autowired
  private UserUtils userUtils;

  @WithMockUser("email@email.com")
  @Test
  void givenWac_whenServletContext_thenItProvidesGreetController() {
    ServletContext servletContext = wac.getServletContext();

    assertNotNull(servletContext);
    assertTrue(servletContext instanceof MockServletContext);
    assertNotNull(wac.getBean("userController"));
  }

  @Test
  void create_whenDataIsValid_thenReturnCreatedUser() throws Exception {
    userUtils.createUserValidatingAndGettingResponse(getMockMvc(), EMAIL, PASSWORD, FIRST_NAME, LAST_NAME,
        SpringRestDocs.UsersPage.CREATE_USER);
  }

  @WithMockUser("email@email.com")
  @Test
  void findUserByEmail_whenUsingValidEmail_thenReturnUserDto() throws Exception {
    userUtils
        .createUserValidatingAndGettingResponse(getMockMvc(), "email@email.com0", "password", "first name0",
            "last name0");
    userUtils
        .createUserValidatingAndGettingResponse(getMockMvc(), "email@email.com1", "password", "first name1",
            "last name1");
    userUtils.findUserByEmail(getMockMvc(), "email@email.com0", SpringRestDocs.UsersPage.GET_USER_BY_EMAIL);
  }

  @WithMockUser("email@email.com")
  @Test
  void findUserById_whenUsingValidId_thenReturnUserDto() throws Exception {
    UserDto userDto =
        userUtils
            .createUserValidatingAndGettingResponse(getMockMvc(), "emailxx@email.com0", "passwordxx", "first namexx",
                "last namexx");
    userUtils.findUserById(getMockMvc(), userDto.getId(), SpringRestDocs.UsersPage.GET_USER_BY_ID);
  }

  @WithMockUser("email@email.com")
  @Test
  void assignLearningWordsToUser_whenFetchingAssignedFirstWords_thenUserIsAssignedWords() throws Exception {
    learningWordRepository.deleteAll();
    wordRepository.deleteAll();
    importer.doImport();

    UserDto userDto =
        userUtils
            .createUserValidatingAndGettingResponse(getMockMvc(), EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
    Long userId = userDto.getId();

    userUtils.assignWordsToUser(getMockMvc(), userId, EMAIL, FIRST_NAME, LAST_NAME, DIE, ZEIT, DAS, ZIMMER,
        SpringRestDocs.UsersPage.ASSIGN_LEARNING_WORDS);
  }
}