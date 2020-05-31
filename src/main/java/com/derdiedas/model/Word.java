package com.derdiedas.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Word entity. It holds entity which will customers will use
 * in the application, to guess their articles and learn their
 * meaning.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Word {

    /**
     * Unique entity identifier. It is generated when the entity
     * is created in the database.
     *
     * @param id JPA generated Identifier
     * @return Unique entity identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Exclude
    private Long id;

    /**
     * Word's article, which the user will have to correctly assert in the application.
     *
     * @param article Word's article.
     * @return Word's article.
     */
    @Column(nullable = false)
    private String article;

    /**
     * The word itself, which the user will have to currently assert its article.
     *
     * @param word The word itself.
     * @return The word itself.
     */
    @Column(nullable = false)
    private String word;

    /**
     * The word's translation, currently in English.
     *
     * @param translation Word's translation.
     * @return Word's translation.
     */
    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    private String translation;

    /**
     * The {@link LearningWord} is a wrapper to this object, holding learning information.
     * This entity is a lnk to all words being learning by all {@link User}s in the system.
     *
     * @param learningWords {@link LearningWord}s associated to users.
     * @return {@link LearningWord}s associated to users.
     */
    @Singular
    @OneToMany(
            mappedBy = "word",
            cascade = CascadeType.ALL
    )
    @EqualsAndHashCode.Exclude
    private Set<LearningWord> learningWords;

    /**
     * Creates a {@link LearningWord} based in this entity. It is
     * necessary to add the learning status of the current word,
     * by this method's parameter.
     *
     * @param isStudied True if the word is learned, false otherwise.
     * @return LearningWord wrapper to the current {@link Word} entity, holding the learning status.
     */
    public LearningWord createLearningWord(boolean isStudied) {
        return LearningWord.builder()
                .isStudied(isStudied)
                .word(this)
                .build();
    }
}
