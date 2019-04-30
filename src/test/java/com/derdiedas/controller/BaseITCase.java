package com.derdiedas.controller;

import com.derdiedas.dto.LearningWordDto;
import com.derdiedas.dto.UserDto;
import com.derdiedas.model.LearningWord;
import com.derdiedas.repository.LearningWordRepository;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.repository.WordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.derdiedas.controller.QueryStringConstants.*;
import static com.derdiedas.model.DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .apply(documentationConfiguration(restDocumentation)).build();
        userRepository.deleteAll();
        learningWordRepository.deleteAll();
        wordRepository.deleteAll();
    }

    protected void studyWord(long learningWordId) throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(put("/learningWords/" + learningWordId)
                        .param("isStudied", Boolean.TRUE.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andDo(document("learning-words/set-status",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        LearningWordDto result = objectMapper.readValue(contentAsString, LearningWordDto.class);

        assertNotNull(result);
        assertTrue(result.isStudied());
    }

    protected UserDto createUser(String email, String password, String firstName, String lastName) throws Exception {
        String body = "{\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"password\": \"" + password + "\",\n" +
                "    \"firstName\": \"" + firstName + "\",\n" +
                "    \"lastName\": \"" + lastName + "\"\n" +
                "}";

        MvcResult mvcResult = this.mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andDo(document("users/create-user",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(contentAsString, UserDto.class);
    }

    protected void findUserByEmail(String email) throws Exception {
        this.mockMvc
                .perform(get("/users").param("fetchType", FETCH_TYPE_EMAIL).param("idOrEmail", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andDo(document("users/get-user-by-email",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    protected void findUserById(Long userId) throws Exception {
        this.mockMvc
                .perform(get("/users").param("fetchType", FETCH_TYPE_ID).param("idOrEmail", userId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andDo(document("users/get-user-by-id",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }

    protected UserDto assignWordsToUser(Long userId, String email, String firstName, String lastName, String firstArticle, String firstWord, String lastArticle, String lastWord) throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(put("/users/" + userId)
                        .param("action", ACTION_ASSIGN_LEARNING_WORDS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andDo(document("users/assign-learning-words",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn();

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

    private boolean wordMatches(LearningWordDto lw, String article, String word) {
        return article.equals(lw.getWord().getArticle()) && word.equals(lw.getWord().getWord());
    }
}
