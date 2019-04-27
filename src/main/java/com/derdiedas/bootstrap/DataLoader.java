package com.derdiedas.bootstrap;

import com.derdiedas.bootstrap.importer.WordsImporter;
import com.derdiedas.model.User;
import com.derdiedas.service.DefaultSettingsService;
import com.derdiedas.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.derdiedas.model.DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;

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

    static final int NUMBER_OF_USERS = 10;

    private final UserService userService;

    private final List<WordsImporter> wordsImporters;

    private final DefaultSettingsService defaultSettingsService;

    @Autowired
    public DataLoader(UserService userService,
                      List<WordsImporter> wordsImporters,
                      DefaultSettingsService defaultSettingsService) {
        this.userService = userService;
        this.wordsImporters = wordsImporters;
        this.defaultSettingsService = defaultSettingsService;

        log.info("Start loading data for embedded application...");

        this.createUsers();
        this.createWords();
        this.setDefaultSettings();

        log.info("Data for embedded application loaded successfully");
    }

    private void setDefaultSettings() {
        defaultSettingsService.createDefaultSettings(DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP);
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
        for (int index = 0; index < NUMBER_OF_USERS; index++) {
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
