package com.derdiedas.service;

import com.derdiedas.model.LearningWord;
import com.derdiedas.model.User;
import com.derdiedas.repository.LearningWordRepository;
import com.derdiedas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service which handles business logic for {@link LearningWord} entity.
 */
@Service
public class LearningWordService {

    private final UserRepository userRepository;
    private final LearningWordRepository learningWordRepository;

    /**
     * Constructor which holds entities to be injected.
     *
     * @param userRepository {@link User} entity repository
     */
    @Autowired
    public LearningWordService(UserRepository userRepository,
                               LearningWordRepository learningWordRepository) {
        this.userRepository = userRepository;
        this.learningWordRepository = learningWordRepository;
    }

    /**
     * Set a {@link LearningWord} entity learned status.
     *
     * @param learningWordId Entity {@link LearningWord} identifier
     * @param isLearned Set true or false for learned status
     * @return Updated {@link LearningWord} entity, with updated learned status.
     */
    public LearningWord setLearningWordLearnedStatus(long learningWordId, boolean isLearned) {
        LearningWord learningWord = learningWordRepository.findById(learningWordId)
                .orElseThrow(() -> new IllegalArgumentException("Entered learningWordId is not associated with any element in the database"));
        learningWord.setStudied(isLearned);
        return learningWordRepository.save(learningWord);
    }

    /**
     * Get user current list of {@link LearningWord} entities.
     *
     * @param userEmail User email
     * @return List of {@link LearningWord} entities from a certain user
     * @throws IllegalArgumentException Exception thrown in in case on user is found by the
     *                                  given email
     */
    public Set<LearningWord> findUserCurrentLearningWords(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("No user found given the user email " + userEmail));

        return user.getLearningWords();
    }
}
