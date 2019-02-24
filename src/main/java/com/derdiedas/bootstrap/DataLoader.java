package com.derdiedas.bootstrap;

import com.derdiedas.model.User;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Every time the application is startup, the H2 database is
 * populated with simple data.
 */
@Component
public class DataLoader {

    private static final String EMAIL = "email@email.com";
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String PASSWORD = "abcde";

    private UserService userService;

    @Autowired
    public DataLoader(UserService userService) {
        this.userService = userService;
        this.loadData();
    }

    private void loadData() {
        for (int index = 0; index < 10; index++) {
            User user = new User();
            user.setEmail(EMAIL + index);
            user.setFirstName(FIRST_NAME + index);
            user.setLastName(LAST_NAME + index);
            user.setPassword(PASSWORD + index);
            userService.save(user);
        }
    }
}
