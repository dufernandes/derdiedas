package com.derdiedas.util;

import com.derdiedas.dto.WordDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DtoUtil {

    public static void verifyWordDtoMan(WordDto wordDto) {
        assertNotNull(wordDto);
        assertEquals(WordUtil.WORD_ID_MAN, wordDto.getId());
        assertEquals(WordUtil.ARTICLE_MAN, wordDto.getArticle());
        assertEquals(WordUtil.WORD_MAN, wordDto.getWord());
        assertEquals(WordUtil.TRANSLATION_MAN, wordDto.getTranslation());
    }

    public static void verifyWordDtoSchool(WordDto wordDto) {
        assertNotNull(wordDto);
        assertEquals(WordUtil.WORD_ID_SCHOOL, wordDto.getId());
        assertEquals(WordUtil.ARTICLE_SCHOOL, wordDto.getArticle());
        assertEquals(WordUtil.WORD_SCHOOL, wordDto.getWord());
        assertEquals(WordUtil.TRANSLATION_SCHOOL, wordDto.getTranslation());
    }
}
