package com.derdiedas.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email@email.com";
    private static final int NUMBER_OF_WORDS = DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;
    private static final long GROUP_PAGE = 2;

    @Test
    void checkUserDetailsMethods_validParameters_returnValidUser() {
        User user = User.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .numberOfWordsPerStudyGroup(NUMBER_OF_WORDS)
                .currentStudyGroupPage(GROUP_PAGE)
                .build();

        assertNotNull(user);
        assertEquals(USER_ID, user.getId());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(NUMBER_OF_WORDS, user.getNumberOfWordsPerStudyGroup());
        assertEquals(GROUP_PAGE, user.getCurrentStudyGroupPage());
        assertEquals(EMAIL, user.getUsername());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}