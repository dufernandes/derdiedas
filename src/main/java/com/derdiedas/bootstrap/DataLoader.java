package com.derdiedas.bootstrap;

import com.derdiedas.bootstrap.importer.WordsImporter;
import com.derdiedas.model.User;
import com.derdiedas.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Every time the application is startup, the H2 database is
 * populated with simple data.
 */
@Slf4j
@Service
public class DataLoader {

    private static final String EMAIL = "email@email.com";
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String PASSWORD = "abcde";

    private UserService userService;

    private List<WordsImporter> wordsImporters;

    @Autowired
    public DataLoader(UserService userService, List<WordsImporter> wordsImporters) {
        this.userService = userService;
        this.wordsImporters = wordsImporters;

        log.info("Start loading data for embedded application...");

        this.createUsers();
        this.createWords();

        log.info("Data for embedded application loaded successfully");
    }

    private void createWords() {
        log.info("Starting words creation...");

        wordsImporters.forEach(wordImporter -> {
            try {
                wordImporter.doImport();
            } catch (IOException ioe) {
                log.error("Problems creating words", ioe);
            }
        });

        log.info("Words created successfully");
    }

    private void createUsers() {
        log.info("Starting users creation...");
        for (int index = 0; index < 10; index++) {
            User user = new User();
            user.setEmail(EMAIL + index);
            user.setFirstName(FIRST_NAME + index);
            user.setLastName(LAST_NAME + index);
            user.setPassword(PASSWORD + index);
            userService.save(user);
        }
        log.info("Users created successfully");
    }
}
