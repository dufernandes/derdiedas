package com.derdiedas.repository;

import com.derdiedas.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository related to the {@link Word} entity.
 */
public interface WordRepository extends CrudRepository<Word, Long> {

    /**
     * Find a {@link Word} based on its article and word itself. If
     * there is no combination, {@link Optional#empty()} is returned.
     *
     * @param article Article of the word
     * @param word    Word itself
     * @return Word entity.
     */
    Optional<Word> findByArticleAndWord(String article, String word);

    /**
     * Find a list of {@link Word} entities. The query is paged using
     * the {@link Pageable} object for doing so.
     *
     * @param pageable Paging is performed using the {@link Pageable} object.
     * @return Entity {@link Page} holding paged the search of {@link Word} elements
     */
    Page<Word> findAll(Pageable pageable);
}
