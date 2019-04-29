package com.derdiedas.service;

import com.derdiedas.model.LearningWord;
import com.derdiedas.model.User;
import com.derdiedas.model.Word;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.repository.WordRepository;
import com.derdiedas.util.UserUtil;
import com.derdiedas.util.WordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.derdiedas.util.UserUtil.EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LearningWordServiceTest {

    @Mock
    private WordRepository wordRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LearningWordService learningWordService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
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

    @Test
    void assignLearningWordsForUser_invalidEmail_throwException() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            learningWordService.assignLearningWordsForUser(EMAIL);
        });

        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void assignLearningWordsForUser_userContainsLearningWords_returnLearningWords() {
        Word school = WordUtil.createWordSchool();
        User user = UserUtil.createUser();

        Page<Word> page = new PageImpl<>(Collections.singletonList(school));

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(wordRepository.findAll(PageRequest.of(user.getStudyGroupPage(), user.getWordsPerGroup())))
                .thenReturn(page);
        when(userRepository.save(user)).thenReturn(user);

        Set<LearningWord> result = learningWordService.assignLearningWordsForUser(EMAIL);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(school.createLearningWord(false), result.iterator().next());
        verify(userRepository).findByEmail(EMAIL);
        verify(wordRepository).findAll(PageRequest.of(user.getStudyGroupPage(), user.getWordsPerGroup()));
        verify(userRepository).save(user);
    }

    @Test
    void assignLearningWordsForUser_userNoLearningWords_returnLearningWords() {
        User user = UserUtil.createUser();
        user.setLearningWords(new HashSet<>());

        Page<Word> page = new PageImpl<>(Collections.emptyList());

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(wordRepository.findAll(PageRequest.of(user.getStudyGroupPage(), user.getWordsPerGroup())))
                .thenReturn(page);
        when(userRepository.save(user)).thenReturn(user);

        Set<LearningWord> result = learningWordService.assignLearningWordsForUser(EMAIL);
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository).findByEmail(EMAIL);
        verify(wordRepository).findAll(PageRequest.of(user.getStudyGroupPage(), user.getWordsPerGroup()));
        verify(userRepository).save(user);
    }

    @Test
    void assignLearningWordsForUser_userLearnedLearningWords_returnLearningWords() {
        Word school = WordUtil.createWordSchool();
        User user = UserUtil.createUser();
        user.getLearningWords().iterator().next().setStudied(true);
        int increasedGroupPage = user.getStudyGroupPage() + 1;

        Page<Word> page = new PageImpl<>(Collections.singletonList(school));

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(wordRepository.findAll(PageRequest.of(increasedGroupPage, user.getWordsPerGroup())))
                .thenReturn(page);
        when(userRepository.save(user)).thenReturn(user);

        Set<LearningWord> result = learningWordService.assignLearningWordsForUser(EMAIL);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(school.createLearningWord(false), result.iterator().next());
        verify(userRepository).findByEmail(EMAIL);
        verify(wordRepository).findAll(PageRequest.of(increasedGroupPage, user.getWordsPerGroup()));
        verify(userRepository).save(user);
    }
}