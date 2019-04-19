package com.derdiedas.repository;

import com.derdiedas.model.Word;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WordRepository extends CrudRepository<Word, Long> {

    Optional<Word> findByArticleAndWord(String article, String germanWord);
}
