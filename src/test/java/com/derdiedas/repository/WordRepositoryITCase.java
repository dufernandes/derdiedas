package com.derdiedas.repository;

import com.derdiedas.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WordRepositoryITCase {

    private static final String DER = "der";
    private static final String MANN = "Mann";
    private static final String THE_MAN = "The man";
    private static final String DIE = "die";
    private static final String ZEIT = "Zeit";
    private static final String THE_TIME = "The time";
    private static final String HAND = "Hand";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WordRepository wordRepository;

    @BeforeEach
    void setup() {
        Word theMan = Word.builder()
                .article(DER)
                .word(MANN)
                .translation(THE_MAN)
                .build();

        entityManager.persistAndFlush(theMan);

        Word theTime = Word.builder()
                .article(DIE)
                .word(ZEIT)
                .translation(THE_TIME)
                .build();

        entityManager.persistAndFlush(theTime);
    }


    @Test
    void findByArticleAndGermanWord_validWord_returnWordObject() {
        assertTrue(wordRepository.findByArticleAndWord(DER, MANN).isPresent());
        assertTrue(wordRepository.findByArticleAndWord(DIE, ZEIT).isPresent());
    }

    @Test
    void findByArticleAndGermanWord_notSavedWord_returnEmptyOptional() {
        assertFalse(wordRepository.findByArticleAndWord(DIE, HAND).isPresent());
    }

    @Test
    void save_missingWordAttributes_throwException() {
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(Word.builder().article(DIE).word(MANN).build());
        });

        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(Word.builder().article(DIE).translation(THE_MAN).build());
        });

        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(Word.builder().translation(THE_MAN).translation(THE_MAN).build());
        });
    }
}
