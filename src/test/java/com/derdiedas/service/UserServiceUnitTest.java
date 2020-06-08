package com.derdiedas.service;

import com.derdiedas.dto.UserToCreateDto;
import com.derdiedas.model.DefaultSettings;
import com.derdiedas.model.User;
import com.derdiedas.model.Word;
import com.derdiedas.repository.DefaultSettingsRepository;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.repository.WordRepository;
import com.derdiedas.util.UserUtil;
import com.derdiedas.util.WordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.function.Function;

import static com.derdiedas.util.DefaultSettingsUtil.createDefaultSettings;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceUnitTest {

    private static final long USER_ID = 3L;
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

    @Mock
    private WordRepository wordRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void assignLearningWordsToUser_whenUserHasAllCurrentWordsToLearn_thenReturnSameLearningWords() {
        Word school = WordUtil.createWordSchool();
        User user = UserUtil.createUser();

        Page<Word> page = new PageImpl<>(Collections.singletonList(school));

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(wordRepository.findAll(PageRequest.of(user.getStudyGroupPage(), user.getWordsPerGroup())))
                .thenReturn(page);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.assignLearningWordsToUser(USER_ID);
        assertNotNull(result);
        assertEquals(1, result.getLearningWords().size());
        assertEquals(school.createLearningWord(false), result.getLearningWords().iterator().next());
        verify(userRepository).findById(USER_ID);
        verify(wordRepository).findAll(PageRequest.of(user.getStudyGroupPage(), user.getWordsPerGroup()));
        verify(userRepository).save(user);
    }

    @Test
    void assignLearningWordsToUser_whenNewLearningWordsLeft_thenReturnNoNewLearningWords() {
        User user = UserUtil.createUser();
        user.setLearningWords(new HashSet<>());

        Page<Word> page = new PageImpl<>(Collections.emptyList());

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(wordRepository.findAll(PageRequest.of(user.getStudyGroupPage(), user.getWordsPerGroup())))
                .thenReturn(page);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.assignLearningWordsToUser(USER_ID);
        assertNotNull(result);
        assertEquals(0, result.getLearningWords().size());
        verify(userRepository).findById(USER_ID);
        verify(wordRepository).findAll(PageRequest.of(user.getStudyGroupPage(), user.getWordsPerGroup()));
        verify(userRepository).save(user);
    }

    @Test
    void assignLearningWordsToUser_whenUserLearnedAllWords_thenReturnNewLearningWords() {
        Word school = WordUtil.createWordSchool();
        User user = UserUtil.createUser();
        user.getLearningWords().iterator().next().setStudied(true);
        int increasedGroupPage = user.getStudyGroupPage() + 1;

        Page<Word> page = new PageImpl<>(Collections.singletonList(school));

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(wordRepository.findAll(PageRequest.of(increasedGroupPage, user.getWordsPerGroup())))
                .thenReturn(page);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.assignLearningWordsToUser(USER_ID);
        assertNotNull(result);
        assertEquals(1, result.getLearningWords().size());
        assertEquals(school.createLearningWord(false), result.getLearningWords().iterator().next());
        verify(userRepository).findById(USER_ID);
        verify(wordRepository).findAll(PageRequest.of(increasedGroupPage, user.getWordsPerGroup()));
        verify(userRepository).save(user);
    }

    @Test
    void assignLearningWordsToUser_whenEmailIsInvalid_thenThrowException() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.assignLearningWordsToUser(USER_ID);
        });

        verify(userRepository).findById(USER_ID);
    }

    @Test
    void createUser_whenParameterIsValid_thenReturnCreatedUser() {
        UserToCreateDto userDto = createMockUserToCreateDto();
        User user = UserToCreateDto.toUser(userDto);
        user.setPassword(ENCODED_PASSWORD);

        DefaultSettings defaultSettings = createDefaultSettings();
        user.setWordsPerGroup(defaultSettings.getDefaultNumberOfWordsPerStudyGroup());
        when(defaultSettingsRepository.findDefault()).thenReturn(defaultSettings);

        mockForSave(user);

        User result = userService.createUser(userDto);

        assertNotNull(result);
        verify(defaultSettingsRepository).findDefault();
        verify(bCryptPasswordEncoder).encode(PASSWORD);
        verify(userRepository).save(user);

    }

    @Test
    void loadUserByUsername_whenUserNameIsValid_ThenReturnUserDetailsObject() {
        User user = createMockUser();

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(EMAIL);
        assertNotNull(result);
        assertEquals(EMAIL, result.getUsername());
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void save_whenUserParameterIsValid_thenReturnSavedUser() {
        User user = createMockUser();

        mockForSave(user);

        User result = userService.save(user);
        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void findUserByEmail_whenLoginIsValid_thenReturnUserMatch() {

        User user = mock(User.class);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        User result = userService.findUserByEmail(EMAIL).orElse(null);
        assertNotNull(result);
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void findUserById_whenIdIsValid_thenReturnUserMatch() {

        User user = mock(User.class);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        User result = userService.findUserById(USER_ID).orElse(null);
        assertNotNull(result);
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void findAllPaged_whenPagedValuesAreValid_thenReturnPagedResults() {
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
