package com.derdiedas.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email@email.com";
    private static final int NUMBER_OF_WORDS = DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;
    private static final int GROUP_PAGE = 2;

    @Test
    void checkUserDetailsMethods_validParameters_returnValidUser() {
        User user = User.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .wordsPerGroup(NUMBER_OF_WORDS)
                .studyGroupPage(GROUP_PAGE)
                .build();

        assertNotNull(user);
        assertEquals(USER_ID, user.getId());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(NUMBER_OF_WORDS, user.getWordsPerGroup());
        assertEquals(GROUP_PAGE, user.getStudyGroupPage());
        assertEquals(EMAIL, user.getUsername());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}