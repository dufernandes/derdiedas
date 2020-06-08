package com.derdiedas.repository;

import com.derdiedas.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
class UserRepositoryITCase {

    private static final String EMAIL = "email@email.com";
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String PASSWORD = "abcde";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByLogin_whenLoginIsValid_thenReturnUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setPassword(PASSWORD);

        entityManager.persist(user);
        User result = userRepository.findByEmail(EMAIL).orElse(null);
        assertNotNull(result);
        assertEquals(EMAIL, result.getEmail());
        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(PASSWORD, result.getPassword());
    }

    @Test
    void findAll_whenExist10UsersPageBy5_thenReturn5Users() {
        List<User> users = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            User user = new User();
            user.setEmail(EMAIL + index);
            user.setFirstName(FIRST_NAME + index);
            user.setLastName(LAST_NAME + index);
            user.setPassword(PASSWORD + index);
            entityManager.persist(user);
        }

        Page<User> result = userRepository.findAll(PageRequest.of(0, 5));
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        users = result.getContent();
        assertEquals(5, users.size());
        for (int index = 0; index < 5; index++) {
            User user = result.iterator().next();
            assertEquals(EMAIL + index, users.get(index).getEmail());
            assertEquals(FIRST_NAME + index, users.get(index).getFirstName());
            assertEquals(LAST_NAME + index, users.get(index).getLastName());
            assertEquals(PASSWORD + index, users.get(index).getPassword());
        }
    }
}