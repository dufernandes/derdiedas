package com.derdiedas.repository;

import com.derdiedas.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.PersistenceException;

import java.util.List;

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
    void findAll_whenExists2Words_thenReturnPagedResult() {
        Page<Word> wordsPage = wordRepository.findAll(PageRequest.of(0, 5));
        List<Word> words = wordsPage.getContent();
        assertNotNull(words);
        assertEquals(2, words.size());
        assertTrue(words.stream()
                .anyMatch(word -> DER.equals(word.getArticle())
                        && MANN.equals(word.getWord())));
        assertTrue(words.stream()
                .anyMatch(word -> DIE.equals(word.getArticle())
                        && ZEIT.equals(word.getWord())));
    }

    @Test
    void findAll_whenMaxPageIsExceeded_thenReturnEmptyPageResult() {
        Page<Word> wordsPage = wordRepository.findAll(PageRequest.of(1, 5));
        List<Word> words = wordsPage.getContent();
        assertNotNull(words);
        assertEquals(0, words.size());
        assertFalse(words.stream()
                .anyMatch(word -> DER.equals(word.getArticle())
                        && MANN.equals(word.getWord())));
        assertFalse(words.stream()
                .anyMatch(word -> DIE.equals(word.getArticle())
                        && ZEIT.equals(word.getWord())));
    }


    @Test
    void findByArticleAndGermanWord_whenWordIsValid_thenReturnWordObject() {
        assertTrue(wordRepository.findByArticleAndWord(DER, MANN).isPresent());
        assertTrue(wordRepository.findByArticleAndWord(DIE, ZEIT).isPresent());
    }

    @Test
    void findByArticleAndGermanWord_whenNoSavedWordExists_thenReturnEmptyOptional() {
        assertFalse(wordRepository.findByArticleAndWord(DIE, HAND).isPresent());
    }

    @Test
    void save_whenDataHasMissingWordAttributes_thenThrowException() {
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
