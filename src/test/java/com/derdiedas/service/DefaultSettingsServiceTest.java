package com.derdiedas.service;

import com.derdiedas.model.DefaultSettings;
import com.derdiedas.repository.DefaultSettingsRepository;
import com.derdiedas.util.DefaultSettingsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.derdiedas.util.DefaultSettingsUtil.createDefaultSettings;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DefaultSettingsServiceTest {

    @Mock
    private DefaultSettingsRepository defaultSettingsRepository;

    @InjectMocks
    private DefaultSettingsService defaultSettingsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createDefaultSettings_validSettings_returnCreatedDefaultSettings() {
        int numberOfWords = 3;
        DefaultSettings settings = createDefaultSettings(numberOfWords);
        when(defaultSettingsRepository.save(settings)).thenReturn(settings);

        DefaultSettings result = defaultSettingsService.createDefaultSettings(numberOfWords);

        assertNotNull(result);
        assertEquals(numberOfWords, result.getDefaultNumberOfWordsPerStudyGroup());
    }
}