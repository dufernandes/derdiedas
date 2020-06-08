package com.derdiedas.repository;

import com.derdiedas.model.DefaultSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.derdiedas.model.DefaultSettings.DEFAULT_NAME;
import static com.derdiedas.model.DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class DefaultSettingsRepositoryITCase {

    @Autowired
    private DefaultSettingsRepository defaultSettingsRepository;

    @BeforeEach
    void setup() {
        defaultSettingsRepository.deleteAll();
        defaultSettingsRepository.save(DefaultSettings.buildWithDefaultParameters());
    }

    @Test
    void findDefault_whenValusAreValid_thenReturnDefaultSettings() {
        DefaultSettings defaultSettings = defaultSettingsRepository.findDefault();
        assertNotNull(defaultSettings);
        assertEquals(DEFAULT_NAME, defaultSettings.getName());
        assertEquals(DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP, defaultSettings.getDefaultNumberOfWordsPerStudyGroup());
    }
}
