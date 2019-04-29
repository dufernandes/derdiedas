package com.derdiedas.util;

import com.derdiedas.model.Word;
import com.derdiedas.model.LearningWord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WordUtil {
    public static final Long WORD_ID_MAN = 2L;
    public static final String ARTICLE_MAN = "der";
    public static final String WORD_MAN = "Mann";
    public static final String TRANSLATION_MAN = "the man";
    public static final Long WORD_ID_SCHOOL = 3L;
    public static final String ARTICLE_SCHOOL = "die";
    public static final String WORD_SCHOOL = "Schule";
    public static final String TRANSLATION_SCHOOL = "the school";

    public static Word createWordMan() {
        return Word.builder()
                .id(WORD_ID_MAN)
                .article(ARTICLE_MAN)
                .word(WORD_MAN)
                .translation(TRANSLATION_MAN)
                .build();
    }

    public static Word createWordSchool() {
        return Word.builder()
                .id(WORD_ID_SCHOOL)
                .article(ARTICLE_SCHOOL)
                .word(WORD_SCHOOL)
                .translation(TRANSLATION_SCHOOL)
                .build();
    }

    public static LearningWord createLearningWordSchool() {
        return createWordSchool().createLearningWord(false);
    }

    public static void verifyWordMan(Word word) {
        assertNotNull(word);
        assertEquals(WORD_ID_MAN, word.getId());
        assertEquals(ARTICLE_MAN, word.getArticle());
        assertEquals(WORD_MAN, word.getWord());
        assertEquals(TRANSLATION_MAN, word.getTranslation());
    }

    public static void verifyLearningWordMan(LearningWord learningWord, boolean isStudied) {
        assertNotNull(learningWord);
        assertEquals(isStudied, learningWord.isStudied());
        assertNotNull(learningWord.getWord());
        verifyWordMan(learningWord.getWord());
    }
}
