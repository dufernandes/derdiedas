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
public class ImporterFromFirstList extends ImporterFirstTxtPattern {

    private static final String TRANSLATION_WORD_SEPARATOR = " – ";
    private static final String TRANSLATION_SEPARATOR = ". ";
    private static final String ARTICLE_WORD_SEPARATOR = " ";

    private static final int TRANSLATION_OUTER_INDEX = 0;
    private static final int TRANSLATION_INNER_INDEX = 1;
    private static final int WORD_OUTER_INDEX = 1;
    private static final int WORD_INNER_INDEX = 1;
    private static final int ARTICLE_INDEX = 0;

    private static final int ARTICLE_WORD_PART_SIZE = 2;

    @Autowired
    public ImporterFromFirstList(WordService wordService) {
        super(wordService);
    }

    @Override
    public String getFilePath() {
        return "/static/firstTxtPattern/germanWordsFirstList.txt";
    }

    @Override
    protected WordDto parseToWord(String line) {
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
        } else {
            log.warn("Word from line '{}' could not be imported because the substantive has no article.", line);
        }

        return wordDto;
    }

    @Override
    public String getSourceName() {
        return "File: " + getFilePath();
    }
}
