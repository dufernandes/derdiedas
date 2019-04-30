package com.derdiedas.service;

import com.derdiedas.model.LearningWord;
import com.derdiedas.model.User;
import com.derdiedas.repository.LearningWordRepository;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.util.UserUtil;
import com.derdiedas.util.WordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static com.derdiedas.util.UserUtil.EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LearningWordServiceTest {

    private static final long LEARNING_WORD_ID = 3L;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LearningWordRepository learningWordRepository;

    @InjectMocks
    private LearningWordService learningWordService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void setLearningWordLearnedStatus_validId_returnUpdatedEntity() {
        LearningWord school = WordUtil.createLearningWordSchool();
        school.setId(LEARNING_WORD_ID);
        LearningWord schoolLearned = WordUtil.createLearningWordSchool();
        schoolLearned.setId(LEARNING_WORD_ID);
        schoolLearned.setStudied(true);

        when(learningWordRepository.findById(LEARNING_WORD_ID)).thenReturn(Optional.of(school));
        when(learningWordRepository.save(schoolLearned)).thenReturn(schoolLearned);

        LearningWord result = learningWordService.setLearningWordLearnedStatus(LEARNING_WORD_ID, true);
        assertNotNull(result);
        assertEquals(schoolLearned, result);
        verify(learningWordRepository).findById(LEARNING_WORD_ID);
        verify(learningWordRepository).save(schoolLearned);
    }

    @Test
    void setLearningWordLearnedStatus_invalidId_throwException() {
        when(learningWordRepository.findById(LEARNING_WORD_ID)).thenReturn(Optional.empty());
        when(learningWordRepository.save(any())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            learningWordService.setLearningWordLearnedStatus(LEARNING_WORD_ID, true);
        });

        verify(learningWordRepository).findById(LEARNING_WORD_ID);
        verify(learningWordRepository, never()).save(any());
    }

    @Test
    void findUserCurrentLearningWords_validEmail_returnLearningWords() {
        LearningWord school = WordUtil.createLearningWordSchool();
        User user = UserUtil.createUser();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Set<LearningWord> result = learningWordService.findUserCurrentLearningWords(EMAIL);

        assertNotNull(result);
        assertEquals(school, result.iterator().next());
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void findUserCurrentLearningWords_invalidEmail_throwException() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            learningWordService.findUserCurrentLearningWords(EMAIL);
        });

        verify(userRepository).findByEmail(EMAIL);
    }
}