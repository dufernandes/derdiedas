package com.derdiedas.controller;

import com.derdiedas.bootstrap.importer.ImporterFromFirstList;
import com.derdiedas.dto.UserDto;
import com.derdiedas.repository.LearningWordRepository;
import com.derdiedas.repository.WordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @WithMockUser("email@email.com")
    @Test
    void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("userController"));
    }

    @WithMockUser("email@email.com")
    @Test
    void findUserByEmail_validEmail_returnUserDto() throws Exception {
        createUser("email@email.com0", "password", "first name0", "last name0");
        createUser("email@email.com1", "password", "first name1", "last name1");
        findUserByEmail("email@email.com0");
    }

    @WithMockUser("email@email.com")
    @Test
    void findUserById_validId_returnUserDto() throws Exception {
        UserDto userDto = createUser("emailxx@email.com0", "passwordxx", "first namexx", "last namexx");
        findUserById(userDto.getId());
    }

    @WithMockUser("email@email.com")
    @Test
    void assignLearningWordsToUser_fetchAssignFirstWords_userAssignedWords() throws Exception {
        learningWordRepository.deleteAll();
        wordRepository.deleteAll();
        importer.doImport();

        UserDto userDto = createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
        Long userId = userDto.getId();

        assignWordsToUser(userId, EMAIL, FIRST_NAME, LAST_NAME, DIE, ZEIT, DAS, ZIMMER);
    }
}