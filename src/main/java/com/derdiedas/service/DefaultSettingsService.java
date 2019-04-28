package com.derdiedas.service;

import com.derdiedas.model.DefaultSettings;
import com.derdiedas.repository.DefaultSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service which handles business logic related to {@link DefaultSettings}.
 */
@Service
public class DefaultSettingsService {

    private final DefaultSettingsRepository defaultSettingsRepository;

    /**
     * Instantiate {@link com.derdiedas.model.DefaultSettings} services
     * injecting all necessary components
     *
     * @param defaultSettingsRepository {@link com.derdiedas.model.DefaultSettings} repository to be injected
     */
    @Autowired
    public DefaultSettingsService(DefaultSettingsRepository defaultSettingsRepository) {
        this.defaultSettingsRepository = defaultSettingsRepository;
    }

    /**
     * Create the default settings used throughout the system. Note that
     * the default name define as {@link DefaultSettings#DEFAULT_NAME} is
     * used to create this entity.
     *
     * @param defaultNumberOfWordsPerStudyGroup default number of words used
     *                                          in each study block.
     * @return Created {@link DefaultSettings}
     */
    public DefaultSettings createDefaultSettings(int defaultNumberOfWordsPerStudyGroup) {

        return defaultSettingsRepository
                .save(DefaultSettings
                        .builder()
                        .defaultNumberOfWordsPerStudyGroup(defaultNumberOfWordsPerStudyGroup)
                        .name(DefaultSettings.DEFAULT_NAME)
                        .build());
    }
}
