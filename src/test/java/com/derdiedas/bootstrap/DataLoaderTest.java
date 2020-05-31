package com.derdiedas.bootstrap;

import com.derdiedas.bootstrap.importer.ImportResult;
import com.derdiedas.bootstrap.importer.WordsImporter;
import com.derdiedas.service.DefaultSettingsService;
import com.derdiedas.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.derdiedas.bootstrap.DataLoader.NUMBER_OF_USERS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@Slf4j
class DataLoaderTest {

    @Mock
    private UserService userService;

    private List<WordsImporter> wordsImporters = Collections.singletonList(new WordsImporterDummy());

    @Mock
    private DefaultSettingsService defaultSettingsService;

    private DataLoader dataLoader;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void constructorCalled_validData_importDataSuccessfully() {
        dataLoader = new DataLoader(userService, wordsImporters, defaultSettingsService);

        mockAndTestImportingData();
    }

    @Test
    void constructorCalled_wordImporterFailed_importDataSuccessfullyWithoutWords() {
        dataLoader = new DataLoader(userService, Collections
                .singletonList(new WordsImporterDummyThrowIoException()), defaultSettingsService);

        mockAndTestImportingData();
    }

    private void mockAndTestImportingData() {
        when(defaultSettingsService.createDefaultSettings(anyInt())).thenReturn(null);
        when(userService.save(any())).thenReturn(null);

        verify(defaultSettingsService).createDefaultSettings(anyInt());
        verify(userService, times(NUMBER_OF_USERS)).save(any());
    }

    private static class WordsImporterDummy implements WordsImporter {

        @Override
        public ImportResult doImport() {
            log.info("Running import method from WordsImporterDummy");
            return ImportResult.builder()
                    .numberOfExistingWordsNotImported(0)
                    .numberOfFailedWordsToImport(0)
                    .numberOfSuccessfullyImportedWords(0)
                    .build();
        }

        @Override
        public String getSourceName() {
            return "WordsImporterDummy";
        }
    }

    private static class WordsImporterDummyThrowIoException implements WordsImporter {

        @Override
        public ImportResult doImport() throws IOException {
            throw new IOException("Exception thrown while calling import from WordsImporterDummyThrowIoException");
        }

        @Override
        public String getSourceName() {
            return "WordsImporterDummyThrowIoException";
        }
    }
}