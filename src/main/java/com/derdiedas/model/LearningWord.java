package com.derdiedas.model;

import lombok.*;

import javax.persistence.*;

/**
 * Wraps the {@link Word} entity holding information
 * of its study status.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LearningWord {

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
     * Determines whether the {@link Word} wrapped here was already studied (true).
     *
     * @param isStudied true if the word was already studied, false otherwise.
     * @return The status of the word regarding of whether it was studied.
     */
    @Column(nullable = false)
    private boolean isStudied;

    /**
     * The {@link Word} object which a user will study.
     *
     * @param word Word to be studied.
     * @return The {@link Word} object to be studied.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Word word;
}
