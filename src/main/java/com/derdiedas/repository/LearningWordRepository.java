package com.derdiedas.repository;

import com.derdiedas.model.LearningWord;
import com.derdiedas.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository related to the {@link LearningWord} entity.
 */
public interface LearningWordRepository extends CrudRepository<LearningWord, Long> { }
