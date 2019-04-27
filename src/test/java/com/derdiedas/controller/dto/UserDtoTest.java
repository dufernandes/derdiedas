package com.derdiedas.controller.dto;

import com.derdiedas.model.User;
import com.derdiedas.util.WordUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    private static final Long USER_ID = 1L;
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email@email.com";

    @Test
    void buildFromUser_validUser_returnUserDto() {
        User user = createUserDto();

        UserDto dto = UserDto.buildFromUser(user);
        verifyUserDto(dto);
    }

    @Test
    void buildFromUser_nullUser_returnNullUserDto() {
        assertNull(UserDto.buildFromUser(null));
    }

    @Test
    void buildListFromUsers_validUsers_returnListDto() {
        User user1 = createUserDto();
        User user2 = createUserDto();
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        List<UserDto> userDtos = UserDto.buildListFromUsers(users);
        assertNotNull(userDtos);
        assertEquals(2, userDtos.size());
        verifyUserDto(userDtos.get(0));
        verifyUserDto(userDtos.get(1));
    }

    @Test
    void buildListFromUsers_nullUsers_returnEmptyListDto() {
        List<UserDto> dtos = UserDto.buildListFromUsers(null);
        assertNotNull(dtos);
        assertEquals(0, dtos.size());
    }

    private void verifyUserDto(UserDto dto) {
        assertNotNull(dto);
        assertEquals(USER_ID, dto.getId());
        assertEquals(EMAIL, dto.getEmail());
        assertEquals(FIRST_NAME, dto.getFirstName());
        assertEquals(LAST_NAME, dto.getLastName());
        assertEquals(WordUtil.WORD_ID_MAN,
                dto.getWordsStudied().iterator().next().getId());
        assertEquals(WordUtil.ARTICLE_MAN,
                dto.getWordsStudied().iterator().next().getArticle());
        assertEquals(WordUtil.WORD_MAN,
                dto.getWordsStudied().iterator().next().getWord());
        assertEquals(WordUtil.TRANSLATION_MAN,
                dto.getWordsStudied().iterator().next().getTranslation());
        assertEquals(WordUtil.WORD_ID_SCHOOL,
                dto.getWordsStudying().iterator().next().getWordDto().getId());
        assertEquals(WordUtil.ARTICLE_SCHOOL,
                dto.getWordsStudying().iterator().next().getWordDto().getArticle());
        assertEquals(WordUtil.WORD_SCHOOL,
                dto.getWordsStudying().iterator().next().getWordDto().getWord());
        assertEquals(WordUtil.TRANSLATION_SCHOOL,
                dto.getWordsStudying().iterator().next().getWordDto().getTranslation());
        assertFalse(dto.getWordsStudying().iterator().next().isStudied());
    }

    private User createUserDto() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setWordsStudied(singleton(WordUtil.createWordMan()));
        user.setWordsStudying(singleton(WordUtil.createWordOnStudySchool()));
        return user;
    }
}
