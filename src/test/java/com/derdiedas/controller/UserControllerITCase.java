package com.derdiedas.controller;

import com.derdiedas.bootstrap.importer.ImporterFromFirstList;
import com.derdiedas.dto.LearningWordDto;
import com.derdiedas.dto.UserDto;
import com.derdiedas.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static com.derdiedas.controller.QueryStringConstants.ACTION_ASSIGN_LEARNING_WORDS;
import static com.derdiedas.model.DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerITCase extends BaseUserITCase {

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
    private WebApplicationContext wac;

    @Autowired
    private ImporterFromFirstList importer;

    @Autowired
    private WordRepository wordRepository;

    @WithMockUser("email@email.com") // sets this user as authenticated - note that this user already exists
    @Test
    void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("userController"));
    }

    @WithMockUser("email@email.com") // sets this user as authenticated - note that this user already exists
    @Test
    void findUserByEmail_validEmail_returnUserDto() throws Exception {
        createUser("email@email.com0", "password", "first name0", "last name0");
        createUser("email@email.com1", "password", "first name1", "last name1");
        findUserByEmail("email@email.com0");
    }

    @WithMockUser("email@email.com") // sets this user as authenticated - note that this user already exists
    @Test
    void assignLearningWordsToUser_fetchAssignFirstWords_userAssignedWords() throws Exception {
        wordRepository.deleteAll();
        importer.doImport();

        UserDto userDto = createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
        Long userId = userDto.getId();

        assignWordsToUser(EMAIL, FIRST_NAME, LAST_NAME, DIE, ZEIT, DAS, ZIMMER, userId);
    }

    private void assignWordsToUser(String email, String firstName, String lastName, String firstArticle, String firstWord, String lastArticle, String lastWord, Long userId) throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(put("/users/" + userId + "?action=" + ACTION_ASSIGN_LEARNING_WORDS)
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
    }

    private boolean wordMatches(LearningWordDto lw, String article, String word) {
        return article.equals(lw.getWord().getArticle()) && word.equals(lw.getWord().getWord());
    }
}