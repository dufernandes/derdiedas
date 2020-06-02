package com.derdiedas.controller;

import static com.derdiedas.authentication.SecurityConstants.HEADER_STRING_AUTHORIZATION;
import static com.derdiedas.controller.QueryStringConstants.ACTION_ASSIGN_LEARNING_WORDS;
import static com.derdiedas.controller.QueryStringConstants.FETCH_TYPE_EMAIL;
import static com.derdiedas.controller.QueryStringConstants.FETCH_TYPE_ID;
import static com.derdiedas.model.DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.derdiedas.authentication.Credentials;
import com.derdiedas.dto.LearningWordDto;
import com.derdiedas.dto.UserDto;
import com.derdiedas.repository.LearningWordRepository;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.repository.WordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class BaseITCase {

  protected MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext wac;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected LearningWordRepository learningWordRepository;

  @Autowired
  protected WordRepository wordRepository;

  @BeforeEach
  public void setUp(WebApplicationContext webApplicationContext,
                    RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(springSecurity()) // enables security for testing
        .apply(documentationConfiguration(restDocumentation))
        .build();
    userRepository.deleteAll();
    learningWordRepository.deleteAll();
    wordRepository.deleteAll();
  }

  protected String authenticateUser(String userName, String password) throws Exception {

    Credentials credentials = new Credentials();
    credentials.setUsername(userName);
    credentials.setPassword(password);
    ObjectMapper jsonTransformer = new ObjectMapper();
    String requestBodyAsJsonString = jsonTransformer.writeValueAsString(credentials);

    MvcResult mvcResult = this.mockMvc
        .perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBodyAsJsonString))
        .andDo(print()).andExpect(status().isOk())
        .andDo(document(SpringRestDocs.LoginPage.LOGIN_SUCCESSFUL,
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
        .andReturn();

    return mvcResult.getResponse().getHeader(HEADER_STRING_AUTHORIZATION);
  }

  protected void studyWord(long learningWordId, String apiDocsId, String authenticationKey) throws Exception {

    HttpHeaders httpHeaders = addAuthTokenToHttpHeaders(authenticationKey);

    ResultActions resultActions = this.mockMvc
        .perform(put("/learningWords/" + learningWordId)
            .headers(httpHeaders)
            .param("isStudied", Boolean.TRUE.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk());

    MvcResult mvcResult = appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);

    String contentAsString = mvcResult.getResponse().getContentAsString();
    LearningWordDto result = objectMapper.readValue(contentAsString, LearningWordDto.class);

    assertNotNull(result);
    assertTrue(result.isStudied());
  }

  protected void studyWord(long learningWordId) throws Exception {
    studyWord(learningWordId, null, null);
  }

  protected void studyWord(long learningWordId, String apiDocsId) throws Exception {
    studyWord(learningWordId, apiDocsId, null);
  }

  protected void studyWordWithAuthKey(long learningWordId, String authenticationKey) throws Exception {
    studyWord(learningWordId, null, authenticationKey);
  }

  protected UserDto createUser(String email, String password, String firstName, String lastName, String apiDocsId)
      throws Exception {
    String body = "{\n" +
        "    \"email\": \"" + email + "\",\n" +
        "    \"password\": \"" + password + "\",\n" +
        "    \"firstName\": \"" + firstName + "\",\n" +
        "    \"lastName\": \"" + lastName + "\"\n" +
        "}";

    ResultActions resultActions = this.mockMvc
        .perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.firstName").value(firstName))
        .andExpect(jsonPath("$.lastName").value(lastName));

    MvcResult mvcResult = appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);

    String contentAsString = mvcResult.getResponse().getContentAsString();
    return objectMapper.readValue(contentAsString, UserDto.class);
  }

  protected UserDto createUser(String email, String password, String firstName, String lastName) throws Exception {
    return createUser(email, password, firstName, lastName, null);
  }

  protected void findUserByEmail(String email, String apiDocsId) throws Exception {
    ResultActions resultActions = this.mockMvc
        .perform(get("/users").param("fetchType", FETCH_TYPE_EMAIL).param("idOrEmail", email))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(email));

    appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);
  }

  protected void findUserByEmail(String email) throws Exception {
    findUserByEmail(email, null);
  }

  protected void findUserById(Long userId, String apiDocsId) throws Exception {
    ResultActions resultActions = this.mockMvc
        .perform(get("/users").param("fetchType", FETCH_TYPE_ID).param("idOrEmail", userId.toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId));

    appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);
  }

  protected void findUserById(Long userId) throws Exception {
    findUserById(userId, null);
  }

  protected UserDto assignWordsToUser(Long userId, String email, String firstName, String lastName, String firstArticle,
                                      String firstWord, String lastArticle, String lastWord, String apiDocsId, String authenticationKey)
      throws Exception {

    HttpHeaders httpHeaders = addAuthTokenToHttpHeaders(authenticationKey);

    ResultActions resultActions = this.mockMvc
        .perform(put("/users/" + userId)
            .headers(httpHeaders)
            .param("action", ACTION_ASSIGN_LEARNING_WORDS)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk());

    MvcResult mvcResult = appendApiDocsIfNecessaryAndReturnMvcResult(apiDocsId, resultActions);

    String contentAsString = mvcResult.getResponse().getContentAsString();

    UserDto result = objectMapper.readValue(contentAsString, UserDto.class);

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

  protected UserDto assignWordsToUser(Long userId, String email, String firstName, String lastName, String firstArticle,
                                      String firstWord, String lastArticle, String lastWord, String apiDocsId) throws Exception {
    return assignWordsToUser(userId, email, firstName, lastName, firstArticle, firstWord, lastArticle, lastWord, apiDocsId, null);
  }

  protected UserDto assignWordsToUser(Long userId, String email, String firstName, String lastName, String firstArticle,
                                      String firstWord, String lastArticle, String lastWord) throws Exception {
    return assignWordsToUser(userId, email, firstName, lastName, firstArticle, firstWord, lastArticle, lastWord, null, null);
  }

  protected UserDto assignWordsToUserWithAuthKey(Long userId, String email, String firstName, String lastName, String firstArticle,
                                      String firstWord, String lastArticle, String lastWord, String authenticationKey) throws Exception {
    return assignWordsToUser(userId, email, firstName, lastName, firstArticle, firstWord, lastArticle, lastWord, null, authenticationKey);
  }

  private MvcResult appendApiDocsIfNecessaryAndReturnMvcResult(String apiDocsId, ResultActions resultActions)
      throws Exception {
    if (isNotEmpty(apiDocsId)) {
      resultActions = resultActions.andDo(document(apiDocsId,
          preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }
    return resultActions.andReturn();
  }

  private boolean wordMatches(LearningWordDto lw, String article, String word) {
    return article.equals(lw.getWord().getArticle()) && word.equals(lw.getWord().getWord());
  }

  private HttpHeaders addAuthTokenToHttpHeaders(String authenticationKey) {
    HttpHeaders httpHeaders = new HttpHeaders();
    if (isNotEmpty(authenticationKey)) {
      httpHeaders.add(HEADER_STRING_AUTHORIZATION, authenticationKey);
    }
    return httpHeaders;
  }
}
