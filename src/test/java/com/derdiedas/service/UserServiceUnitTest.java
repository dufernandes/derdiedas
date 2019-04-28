package com.derdiedas.service;

import com.derdiedas.dto.UserToCreateDto;
import com.derdiedas.model.DefaultSettings;
import com.derdiedas.model.User;
import com.derdiedas.repository.DefaultSettingsRepository;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.util.DefaultSettingsUtil;
import org.hibernate.validator.constraints.Mod10Check;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static com.derdiedas.util.DefaultSettingsUtil.createDefaultSettings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


class UserServiceUnitTest {

    private static final String EMAIL = "login";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded_password";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private DefaultSettingsRepository defaultSettingsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createUser_validParameter_returnCreatedUser() {
        UserToCreateDto userDto = createMockUserToCreateDto();
        User user = UserToCreateDto.toUser(userDto);
        user.setPassword(ENCODED_PASSWORD);

        DefaultSettings defaultSettings = createDefaultSettings();
        user.setNumberOfWordsPerStudyGroup(defaultSettings.getDefaultNumberOfWordsPerStudyGroup());
        when(defaultSettingsRepository.findDefault()).thenReturn(defaultSettings);

        mockForSave(user);

        User result = userService.createUser(userDto);

        assertNotNull(result);
        verify(defaultSettingsRepository).findDefault();
        verify(bCryptPasswordEncoder).encode(PASSWORD);
        verify(userRepository).save(user);

    }

    @Test
    void loadUserByUsername_validUserName_returnUserDetailsObject() {
        User user = createMockUser();

        when(userRepository.findByEmail(EMAIL)).thenReturn(user);

        UserDetails result = userService.loadUserByUsername(EMAIL);
        assertNotNull(result);
        assertEquals(EMAIL, result.getUsername());
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void save_validParameter_returnSavedParameter() {
        User user = createMockUser();

        mockForSave(user);

        User result = userService.save(user);
        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void findByEmail_validLogin_returnUser() {

        User user = mock(User.class);

        when(userRepository.findByEmail(EMAIL)).thenReturn(user);

        User result = userService.findByEmail(EMAIL);
        assertNotNull(result);
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void findAllPaged_pagedValues_returnPagedResults() {
        when(userRepository.findAll(any())).thenReturn(userPage);

        List<User> result = userService.findAllPaged(0, 3);
        assertEquals(1, result.size());
        verify(userRepository).findAll(any());
    }

    private void mockForSave(User user) {
        when(bCryptPasswordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(user)).thenReturn(user);
    }

    private User createMockUser() {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getUsername()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        return user;
    }

    private UserToCreateDto createMockUserToCreateDto() {
        UserToCreateDto user = mock(UserToCreateDto.class);
        when(user.getEmail()).thenReturn(EMAIL);
        when(user.getPassword()).thenReturn(PASSWORD);
        return user;
    }

    private Page<User> userPage = new Page<User>() {
        @Override
        public int getTotalPages() {
            return 0;
        }

        @Override
        public long getTotalElements() {
            return 0;
        }

        @Override
        public <U> Page<U> map(Function<? super User, ? extends U> function) {
            return null;
        }

        @Override
        public int getNumber() {
            return 0;
        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public int getNumberOfElements() {
            return 0;
        }

        @Override
        public List<User> getContent() {
            return Collections.singletonList(new User());
        }

        @Override
        public boolean hasContent() {
            return false;
        }

        @Override
        public Sort getSort() {
            return null;
        }

        @Override
        public boolean isFirst() {
            return false;
        }

        @Override
        public boolean isLast() {
            return false;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public Pageable nextPageable() {
            return null;
        }

        @Override
        public Pageable previousPageable() {
            return null;
        }

        @Override
        public Iterator<User> iterator() {
            return null;
        }
    };
}
