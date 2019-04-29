package com.derdiedas.util;

import com.derdiedas.model.DefaultSettings;
import com.derdiedas.model.User;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserUtil {

    private static final Long USER_ID = 1L;
    public static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    public static final int NUMBER_OF_WORDS = DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;
    public static final int GROUP_PAGE = 2;

    public static User createUser() {
        return User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .studyGroupPage(GROUP_PAGE)
                .wordsPerGroup(NUMBER_OF_WORDS)
                .learningWords(singleton(WordUtil.createLearningWordSchool()))
                .build();
    }

    public static void verifyUser(User user) {
        assertNotNull(user);
        assertEquals(USER_ID, user.getId());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(NUMBER_OF_WORDS, user.getWordsPerGroup());
        assertEquals(GROUP_PAGE, user.getStudyGroupPage());
    }
}
