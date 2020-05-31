package com.derdiedas.bootstrap.importer;

import com.derdiedas.service.WordService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

@Slf4j
@Service
@Transactional
public abstract class ImporterFirstTxtPattern implements WordsImporter {

    private final WordService wordService;

    @Autowired
    public ImporterFirstTxtPattern(WordService wordService) {
        this.wordService = wordService;
    }

    public abstract String getFilePath();

    @Override
    public ImportResult doImport() throws IOException {
        File dataSource = new File(this.getClass().getResource(getFilePath()).getFile());

        long numberOfSuccessfullyImportedWords = 0L;
        long numberOfFailedWordsToImport = 0L;
        long numberOfExistingWordsNotImported = 0L;

        try (FileInputStream inputStream = new FileInputStream(dataSource.getPath());
             Scanner scanner = new Scanner(inputStream, "UTF-8")) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                WordDto word = parseToWord(line);
                if (word == null) {
                    numberOfFailedWordsToImport++;
                    continue;
                }
                if (!wordService.wordExists(word.getArticle(), word.getWord())) {
                    wordService.createWord(word.getArticle(), word.getWord(), word.getTranslation());
                    numberOfSuccessfullyImportedWords++;
                } else {
                    numberOfExistingWordsNotImported++;
                }
            }

            // note that Scanner suppresses exceptions
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }
        return ImportResult.builder()
                .numberOfSuccessfullyImportedWords(numberOfSuccessfullyImportedWords)
                .numberOfFailedWordsToImport(numberOfFailedWordsToImport)
                .numberOfExistingWordsNotImported(numberOfExistingWordsNotImported)
                .build();
    }

    protected abstract WordDto parseToWord(String line);

    @Getter
    @Setter
    @AllArgsConstructor
    protected static class WordDto {
        private String article;
        private String word;
        private String translation;
    }
}
