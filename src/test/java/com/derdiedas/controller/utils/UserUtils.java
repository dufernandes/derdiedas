package com.derdiedas.controller.utils;

import static com.derdiedas.controller.QueryStringConstants.ACTION_ASSIGN_LEARNING_WORDS;
import static com.derdiedas.controller.QueryStringConstants.FETCH_TYPE_EMAIL;
import static com.derdiedas.controller.QueryStringConstants.FETCH_TYPE_ID;
import static com.derdiedas.model.DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derdiedas.dto.LearningWordDto;
import com.derdiedas.dto.UserDto;
import com.derdiedas.dto.UserToCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserUtils {

  private final ApiDocsUtils apiDocsUtils;
  private final HttpHeadersUtils httpHeadersUtils;
  private final ObjectMapper objectMapper;
  private final AssertionUtils assertionUtils;

  @Autowired
  public UserUtils(ApiDocsUtils apiDocsUtils,
                   HttpHeadersUtils httpHeadersUtils,
                   AssertionUtils assertionUtils,
                   ObjectMapper objectMapper) {
    this.apiDocsUtils = apiDocsUtils;
    this.httpHeadersUtils = httpHeadersUtils;
    this.assertionUtils = assertionUtils;
    this.objectMapper = objectMapper;
  }

  public UserDto createUserValidatingAndGettingResponse(MockMvc mockMvc, String email, String password, String firstName, String lastName,
                                                        String apiDocsId) throws Exception {

    MvcResult mvcResult = createUser(mockMvc, email, password, firstName, lastName, apiDocsId);

    if (!assertionUtils.assertHttpResponseIsOk(mvcResult)) {
      return null;
    }

    String contentAsString = mvcResult.getResponse().getContentAsString();

    objectMapper.readValue(contentAsString, UserDto.class);
    UserDto userDto = objectMapper.readValue(contentAsString, UserDto.class);

    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    assertAll(
        () -> assertEquals(email, userDto.getEmail()),
        () -> assertEquals(firstName, userDto.getFirstName()),
        () -> assertEquals(lastName, userDto.getLastName())
    );
    return userDto;
  }

  public MvcResult createUser(MockMvc mockMvc, String email, String password, String firstName, String lastName,
                               String apiDocsId) throws Exception {
    UserToCreateDto userToCreateDto = UserToCreateDto.builder()
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    String requestBodyAsJsonString = objectMapper.writeValueAsString(userToCreateDto);

    ResultActions resultActions = mockMvc
        .perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBodyAsJsonString))
        .andDo(print());

    return apiDocsUtils.appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);
  }

  public UserDto createUserValidatingAndGettingResponse(MockMvc mockMvc, String email, String password, String firstName, String lastName)
      throws Exception {
    return createUserValidatingAndGettingResponse(mockMvc, email, password, firstName, lastName, null);
  }

  public void findUserByEmail(MockMvc mockMvc, String email, String apiDocsId) throws Exception {
    ResultActions resultActions = mockMvc
        .perform(get("/users").param("fetchType", FETCH_TYPE_EMAIL).param("idOrEmail", email))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(email));

    apiDocsUtils.appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);
  }

  public void findUserByEmail(MockMvc mockMvc, String email) throws Exception {
    findUserByEmail(mockMvc, email, null);
  }

  public void findUserById(MockMvc mockMvc, Long userId, String apiDocsId) throws Exception {
    ResultActions resultActions = mockMvc
        .perform(get("/users").param("fetchType", FETCH_TYPE_ID).param("idOrEmail", userId.toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId));

    apiDocsUtils.appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);
  }

  public void findUserById(MockMvc mockMvc, Long userId) throws Exception {
    findUserById(mockMvc, userId, null);
  }

  public UserDto assignWordsToUser(MockMvc mockMvc, Long userId, String email, String firstName, String lastName,
                                   String firstArticle,
                                   String firstWord, String lastArticle, String lastWord, String apiDocsId,
                                   String authenticationKey)
      throws Exception {

    UserDto result = assignWordsToUser(mockMvc, userId, apiDocsId, authenticationKey);

    assertNotNull(result);
    assertEquals(userId, result.getId());
    assertEquals(email, result.getEmail());
    assertEquals(firstName, result.getFirstName());
    assertEquals(lastName, result.getLastName());
    assertNotNull(result.getWordsStudying());
    assertEquals(DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP, result.getWordsStudying().size());
    assertTrue(result.getWordsStudying()
        .stream()
        .anyMatch(lw -> wordMatches(lw, firstArticle, firstWord)));
    assertTrue(result.getWordsStudying()
        .stream()
        .anyMatch(lw -> wordMatches(lw, lastArticle, lastWord)));

    return result;
  }

  public UserDto assignWordsToUser(MockMvc mockMvc, Long userId, String apiDocsId, String authenticationKey)
      throws Exception {
    HttpHeaders httpHeaders = httpHeadersUtils.addAuthTokenToHttpHeaders(authenticationKey);

    ResultActions resultActions = mockMvc
        .perform(put("/users/" + userId)
            .headers(httpHeaders)
            .param("action", ACTION_ASSIGN_LEARNING_WORDS)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk());

    MvcResult mvcResult = apiDocsUtils.appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);

    String contentAsString = mvcResult.getResponse().getContentAsString();

    return objectMapper.readValue(contentAsString, UserDto.class);
  }

  public UserDto assignWordsToUser(MockMvc mockMvc, Long userId, String authenticationKey) throws Exception {
    return assignWordsToUser(mockMvc, userId, null, authenticationKey);
  }

  public UserDto assignWordsToUser(MockMvc mockMvc, Long userId, String email, String firstName, String lastName,
                                   String firstArticle,
                                   String firstWord, String lastArticle, String lastWord, String apiDocsId)
      throws Exception {
    return assignWordsToUser(mockMvc, userId, email, firstName, lastName, firstArticle, firstWord, lastArticle,
        lastWord,
        apiDocsId, null);
  }

  public UserDto assignWordsToUser(MockMvc mockMvc, Long userId, String email, String firstName, String lastName,
                                   String firstArticle,
                                   String firstWord, String lastArticle, String lastWord) throws Exception {
    return assignWordsToUser(mockMvc, userId, email, firstName, lastName, firstArticle, firstWord, lastArticle,
        lastWord, null,
        null);
  }

  public UserDto assignWordsToUserWithAuthKey(MockMvc mockMvc, Long userId, String email, String firstName,
                                              String lastName,
                                              String firstArticle,
                                              String firstWord, String lastArticle, String lastWord,
                                              String authenticationKey) throws Exception {
    return assignWordsToUser(mockMvc, userId, email, firstName, lastName, firstArticle, firstWord, lastArticle,
        lastWord, null,
        authenticationKey);
  }

  private boolean wordMatches(LearningWordDto lw, String article, String word) {
    return article.equals(lw.getWord().getArticle()) && word.equals(lw.getWord().getWord());
  }
}
