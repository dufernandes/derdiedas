package com.derdiedas.service;

import com.derdiedas.model.User;
import com.derdiedas.model.Word;
import com.derdiedas.model.LearningWord;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * Service which handles business logic for {@link LearningWord} entity.
 */
@Service
public class LearningWordService {

    private final WordRepository wordRepository;

    private final UserRepository userRepository;

    /**
     * Constructor which holds entities to be injected.
     *
     * @param wordRepository {@link Word} entity repository
     * @param userRepository {@link User} entity repository
     */
    @Autowired
    public LearningWordService(WordRepository wordRepository,
                               UserRepository userRepository) {
        this.wordRepository = wordRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get user current list of {@link LearningWord} entities.
     *
     * @param userEmail User email
     * @return List of {@link LearningWord} entities from a certain user
     *
     * @throws IllegalArgumentException Exception thrown in in case on user is found by the
     * given email
     */
    public Set<LearningWord> findUserCurrentLearningWords(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("No user found given the user email " + userEmail));

        return user.getLearningWords();
    }

    public Set<LearningWord> assignLearningWordsForUser(String userEmail) {
        Set<LearningWord> learningWords = new HashSet<>();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("No user found given the user email " + userEmail));

        /*
        First clause - No words being study means that we simply need to the the ones
        configured in the User entity.

        Second clause - At least one word was not studied, thus retrieve the current list
         */
        if (isEmpty(user.getLearningWords()) ||
            user.getLearningWords().stream().anyMatch(word -> !word.isStudied())) {

            learningWords = getLearningWordsAndSaveUser(user);

        } else if (user.getLearningWords().stream().allMatch(LearningWord::isStudied)) {
            /*
            For this case, all words were studied, so the next set must be retrieved
             */
            int groupPage = user.getStudyGroupPage();
            user.setStudyGroupPage(++groupPage);

            learningWords = getLearningWordsAndSaveUser(user);

        } else {
            throw new IllegalStateException("User words status are in an unpredictable state. Please contact support");
        }

        return learningWords;
    }

    private Set<LearningWord> getLearningWordsAndSaveUser(User user) {

        Set<LearningWord> learningWords = getLearningWordsFromWords(user.getStudyGroupPage(), user.getWordsPerGroup());

        user.setLearningWords(learningWords);
        userRepository.save(user);

        return learningWords;
    }

    private Set<LearningWord> getLearningWordsFromWords(int currentStudyGroup, int numberOfWords) {
        Set<Word> wordsStudying = new HashSet<>(wordRepository
                .findAll(PageRequest.of(currentStudyGroup, numberOfWords))
                .getContent());

        return wordsStudying
                .stream()
                .map(word -> word.createLearningWord(false))
                .collect(Collectors.toSet());
    }
}
