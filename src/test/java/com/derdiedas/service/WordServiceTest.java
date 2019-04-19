package com.derdiedas.service;

import com.derdiedas.model.Word;
import com.derdiedas.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WordServiceTest {

    private static final String DER = "der";
    private static final String MANN = "Mann";
    private static final String THE_MAN = "The man";

    @InjectMocks
    private WordService wordService;

    @Mock
    private WordRepository wordRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createWord_parametersForNewWord_returnCreatedWord() {
        Word theMan = Word.builder().article(DER).word(MANN).translation(THE_MAN).build();

        when(wordRepository.findByArticleAndWord(DER, MANN)).thenReturn(Optional.empty());
        when(wordRepository.save(theMan)).thenReturn(theMan);

        Word result = wordService.createWord(DER, MANN, THE_MAN);

        assertNotNull(result);
        assertEquals(theMan, result);
        verify(wordRepository).findByArticleAndWord(DER, MANN);
        verify(wordRepository).save(theMan);
    }

    @Test
    void createWord_parametersWithExistingWord_ExceptionThrown() {
        Word theMan = Word.builder().article(DER).word(MANN).translation(THE_MAN).build();

        when(wordRepository.findByArticleAndWord(DER, MANN)).thenReturn(Optional.of(theMan));
        when(wordRepository.save(theMan)).thenReturn(theMan);

        assertThrows(IllegalArgumentException.class, () -> {
            wordService.createWord(DER, MANN, THE_MAN);
        });

        verify(wordRepository).findByArticleAndWord(DER, MANN);
        verify(wordRepository, never()).save(theMan);
    }
}