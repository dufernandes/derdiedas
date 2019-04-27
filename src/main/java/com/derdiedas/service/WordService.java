package com.derdiedas.service;

import com.derdiedas.model.Word;
import com.derdiedas.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * Service which handles business logic related
 * to the {@link com.derdiedas.model.Word} Entity.
 */

@Service
public class WordService {

    private final WordRepository wordRepository;

    @Autowired
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    /**
     * Create a word based on the entered parameters. If the word
     * already exists, {@link IllegalArgumentException} is thrown.
     *
     * @param article     Article for the word
     * @param word        word itself
     * @param translation Word translation
     * @return Word created object.
     * @throws IllegalArgumentException Exception thrown if the entered parameters already defined a word
     *                                  stored in the database. Note that the only arguments for checking are article and word itself. The translation
     *                                  is irrelevant for the comparison.
     */
    public Word createWord(String article, String word, String translation) {

        if (wordExists(article, word)) {
            throw new IllegalArgumentException(MessageFormat.format("There already is a word with article {0} and german word {1}", article, word));
        }

        Word newWord = Word.builder()
                .article(article)
                .word(word)
                .translation(translation)
                .build();

        return wordRepository.save(newWord);
    }

    public boolean wordExists(String article, String word) {
        return wordRepository.findByArticleAndWord(article, word).isPresent();
    }
}
