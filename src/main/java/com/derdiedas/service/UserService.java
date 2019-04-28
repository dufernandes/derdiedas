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
        user.setNumberOfWordsPerStudyGroup(settings.getDefaultNumberOfWordsPerStudyGroup());

        return save(user);
    }

    public User findByEmail(String login) {
        return userRepository.findByEmail(login);
    }

    public List<User> findAllPaged(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return findByEmail(userName);
    }
}
