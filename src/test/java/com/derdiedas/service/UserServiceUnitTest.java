package com.derdiedas.service;

import com.derdiedas.model.User;
import com.derdiedas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


class UserServiceUnitTest {

    private static final String EMAIL = "login";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void save_validParamter_returnSavedParameter() {

        User user = mock(User.class);

        when(userRepository.save(user)).thenReturn(user);

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
