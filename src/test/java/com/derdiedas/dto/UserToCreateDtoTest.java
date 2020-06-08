package com.derdiedas.dto;

import com.derdiedas.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserToCreateDtoTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";

    @Test
    void toUser_whenParameterIsValid_thenReturnEntity() {
        UserToCreateDto dto = UserToCreateDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        User user = UserToCreateDto.toUser(dto);
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
    }

    @Test
    void toUser_whenParameterIsNull_thenReturnNull() {
        assertNull(UserToCreateDto.toUser(null));
    }
}