package com.derdiedas.controller.dto;

import com.derdiedas.model.Word;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DtoUtil {

    static final Long WORD_ID_MAN = 2L;
    static final String ARTICLE_MAN = "der";
    static final String WORD_MAN = "Mann";
    static final String TRANSLATION_MAN = "the man";

    static final Long WORD_ID_SCHOOL = 3L;
    static final String ARTICLE_SCHOOL = "die";
    static final String WORD_SCHOOL = "Schule";
    static final String TRANSLATION_SCHOOL = "the school";

    static Word createWordMan() {
        return Word.builder()
                .id(WORD_ID_MAN)
                .article(ARTICLE_MAN)
                .word(WORD_MAN)
                .translation(TRANSLATION_MAN)
                .build();
    }

    static Word createWordSchool() {
        return Word.builder()
                .id(WORD_ID_SCHOOL)
                .article(ARTICLE_SCHOOL)
                .word(WORD_SCHOOL)
                .translation(TRANSLATION_SCHOOL)
                .build();
    }

    static void verifyWordDtoMan(WordDto wordDto) {
        assertNotNull(wordDto);
        assertEquals(WORD_ID_MAN, wordDto.getId());
        assertEquals(ARTICLE_MAN, wordDto.getArticle());
        assertEquals(WORD_MAN, wordDto.getWord());
        assertEquals(TRANSLATION_MAN, wordDto.getTranslation());
    }

    static void verifyWordDtoSchool(WordDto wordDto) {
        assertNotNull(wordDto);
        assertEquals(WORD_ID_SCHOOL, wordDto.getId());
        assertEquals(ARTICLE_SCHOOL, wordDto.getArticle());
        assertEquals(WORD_SCHOOL, wordDto.getWord());
        assertEquals(TRANSLATION_SCHOOL, wordDto.getTranslation());
    }
}
