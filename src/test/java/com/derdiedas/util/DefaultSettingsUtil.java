package com.derdiedas.util;

import com.derdiedas.model.DefaultSettings;

import static com.derdiedas.model.DefaultSettings.DEFAULT_NAME;
import static com.derdiedas.model.DefaultSettings.DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP;

public class DefaultSettingsUtil {

    public static DefaultSettings createDefaultSettings() {
        return createDefaultSettings(DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP);
    }

    public static DefaultSettings createDefaultSettings(int numberOfWords) {
        return DefaultSettings
                .builder()
                .name(DEFAULT_NAME)
                .defaultNumberOfWordsPerStudyGroup(numberOfWords)
                .build();
    }
}
