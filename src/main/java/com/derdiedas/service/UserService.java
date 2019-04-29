package com.derdiedas.service;

import com.derdiedas.dto.UserToCreateDto;
import com.derdiedas.model.DefaultSettings;
import com.derdiedas.model.User;
import com.derdiedas.repository.DefaultSettingsRepository;
import com.derdiedas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service which holds business logic methods for the {@link User} entity.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DefaultSettingsRepository defaultSettingsRepository;

    /**
     * Constructor which injects objects for tasks.
     *
     * @param userRepository {@link User} repository
     * @param bCryptPasswordEncoder Password encryption mechanism
     * @param defaultSettingsRepository {@link DefaultSettings} repository
     */
    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       DefaultSettingsRepository defaultSettingsRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.defaultSettingsRepository = defaultSettingsRepository;
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
     *
     * @return User created entity.
     */
    public User createUser(UserToCreateDto userDto) {
        User user = UserToCreateDto.toUser(userDto);
        DefaultSettings settings = defaultSettingsRepository.findDefault();
        user.setWordsPerGroup(settings.getDefaultNumberOfWordsPerStudyGroup());

        return save(user);
    }

    /**
     * Find user by its email. If none if found, {@link Optional#empty()}
     * is returned.
     *
     * @param email email which identifes the {@link User}
     * @return Optional object holding a {@link User}
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find a list of {@link User} entities, doing a paged search.
     *
     * @param offset Offset of the search
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
     * given its userName
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("No user was found with the userName " + userName));
    }
}
