package com.derdiedas.bootstrap.importer;

import com.derdiedas.service.WordService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

@Service
@Transactional
public class ImporterFromFirstList implements WordsImporter {

    private static final String TRANSLATION_WORD_SEPARATOR = " – ";
    private static final String TRANSLATION_SEPARATOR = ". ";
    private static final String ARTICLE_WORD_SEPARATOR = " ";

    private static final int TRANSLATION_OUTER_INDEX = 0;
    private static final int TRANSLATION_INNER_INDEX = 1;
    private static final int WORD_OUTER_INDEX = 1;
    private static final int WORD_INNER_INDEX = 1;
    private static final int ARTICLE_INDEX = 0;

    private static final int ARTICLE_WORD_PART_SIZE = 2;

    private final WordService wordService;

    @Autowired
    public ImporterFromFirstList(WordService wordService) {
        this.wordService = wordService;
    }

    @Override
    public void doImport() throws IOException {
        File dataSource = new File(this.getClass().getResource("/static/germanWordsFirstList.txt").getFile());

        try (FileInputStream inputStream = new FileInputStream(dataSource.getPath());
             Scanner scanner = new Scanner(inputStream, "UTF-8")) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                WordDto word = parseToWord(line);
                if (word != null && !wordService.wordExists(word.getArticle(), word.getWord())) {
                    wordService.createWord(word.getArticle(), word.getWord(), word.getTranslation());
                }
            }

            // note that Scanner suppresses exceptions
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }
    }

    private WordDto parseToWord(String line) {

        WordDto wordDto = null;

        String[] wordParts = line.split(TRANSLATION_WORD_SEPARATOR);
        String translation = wordParts[TRANSLATION_OUTER_INDEX]
                .split(TRANSLATION_SEPARATOR)[TRANSLATION_INNER_INDEX].trim();

        String[] articleWord = wordParts[WORD_OUTER_INDEX].split(ARTICLE_WORD_SEPARATOR);
        // words without article will not be added
        if (articleWord.length == ARTICLE_WORD_PART_SIZE) {
            String article = articleWord[ARTICLE_INDEX].trim().toLowerCase();
            String word = articleWord[WORD_INNER_INDEX].trim();
            wordDto = new WordDto(article, word, translation);
        }

        return wordDto;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class WordDto {
        private String article;
        private String word;
        private String translation;
    }
}
