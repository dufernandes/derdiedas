package com.derdiedas.service;

import com.derdiedas.dto.UserToCreateDto;
import com.derdiedas.model.DefaultSettings;
import com.derdiedas.model.LearningWord;
import com.derdiedas.model.User;
import com.derdiedas.model.Word;
import com.derdiedas.repository.DefaultSettingsRepository;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * Service which holds business logic methods for the {@link User} entity.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DefaultSettingsRepository defaultSettingsRepository;
    private final WordRepository wordRepository;

    /**
     * Constructor which injects objects for tasks.
     *
     * @param userRepository            {@link User} repository
     * @param bCryptPasswordEncoder     Password encryption mechanism
     * @param defaultSettingsRepository {@link DefaultSettings} repository
     */
    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       DefaultSettingsRepository defaultSettingsRepository,
                       WordRepository wordRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.defaultSettingsRepository = defaultSettingsRepository;
        this.wordRepository = wordRepository;
    }

    /**
     * Assign the next {@link LearningWord} entities to a {@link User}.
     * If the {@link User}, has no {@link LearningWord} entities related,
     * the first ones will be set. In another case, if not all {@link LearningWord}
     * entities are marked as studied, the current {@link LearningWord} elements will be
     * returned. Finally, if all {@link LearningWord} elements are marked as studies,
     * then the next ones will be assigned.
     *
     * @param userId User identified.
     * @return Entity {@link User} with the updated relationship with the {@link LearningWord}
     * elements.
     * @throws IllegalArgumentException Exception thrown in case no user is related to the entered id
     * @throws IllegalStateException Exception thrown in case the user is in an unexpected state
     */
    public User assignLearningWordsToUser(long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found given the user email " + userId));

        /*
        First clause - No words being study means that we simply need to the the ones
        configured in the User entity.

        Second clause - At least one word was not studied, thus retrieve the current list
         */
        Set<LearningWord> learningWords = user.getLearningWords();
        if (isEmpty(learningWords) || learningWords.stream().anyMatch(word -> !word.isStudied())) {

            user = assignLearningWordsToUser(user);

        } else if (learningWords.stream().allMatch(LearningWord::isStudied)) {
            /*
            For this case, all words were studied, so the next set must be retrieved
             */
            int groupPage = user.getStudyGroupPage();
            user.setStudyGroupPage(++groupPage);

            user = assignLearningWordsToUser(user);

        } else {
            throw new IllegalStateException("User words status are in an unpredictable state.");
        }

        return user;
    }

    /**
     * Save user encoding his/her password using B-Crypt.
     *
     * @param user User to be saved.
     * @return Stored user. Note that the password is now encrypted.
     */
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Create a user from a DTO, inserting default settings.
     *
     * @param userDto DTO which comes from the API.
     * @return User created entity.
     */
    public User createUser(UserToCreateDto userDto) {
        User user = UserToCreateDto.toUser(userDto);
        DefaultSettings settings = defaultSettingsRepository.findDefault();
        user.setWordsPerGroup(settings.getDefaultNumberOfWordsPerStudyGroup());

        return save(user);
    }

    /**
     * Find user by its id. If none if found, {@link Optional#empty()}
     * is returned.
     *
     * @param id User identifier
     * @return Optional object holding a {@link User}
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Find user by its email. If none if found, {@link Optional#empty()}
     * is returned.
     *
     * @param email email which identifies the {@link User}
     * @return Optional object holding a {@link User}
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find a list of {@link User} entities, doing a paged search.
     *
     * @param offset        Offset of the search
     * @param numberOfUsers Number of users to be retrieved
     * @return List of {@link User} entities
     */
    public List<User> findAllPaged(int offset, int numberOfUsers) {
        return userRepository.findAll(PageRequest.of(offset, numberOfUsers)).getContent();
    }

    /**
     * Find a {@link UserDetails} object based on the entered userName.
     * Note that this method is used in the interface {@link UserDetailsService},
     * which spring authentication frameworks uses.
     *
     * @param userName User Name - in our case, user email
     * @return Entity {@link UserDetails}
     * @throws UsernameNotFoundException Exception thrown if no user is found
     *                                   given its userName
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return findUserByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("No user was found with the userName " + userName));
    }

    private User assignLearningWordsToUser(User user) {

        Set<LearningWord> learningWords = getLearningWordsFromWords(user.getStudyGroupPage(), user.getWordsPerGroup());

        user.setLearningWords(learningWords);
        return userRepository.save(user);
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
