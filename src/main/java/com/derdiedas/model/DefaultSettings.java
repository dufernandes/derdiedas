package com.derdiedas.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Entity which holds default settings used throughout the system.
 */
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefaultSettings {

    /**
     * Default number of words per study group
     */
    public static final int DEFAULT_NUMBER_OF_WORDS_PER_STUDY_GROUP = 10;

    public static final String DEFAULT_NAME = "settingsName";

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
     * Default number of words used in each study group.
     *
     * @param defaultNumberOfWordsPerStudyGroup default number of
     * words used in
     * study groups to be set
     * @return default number of word used in study groups
     */
    @Positive
    @Column(nullable = false)
    private int defaultNumberOfWordsPerStudyGroup;

    @NotNull
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;
}
