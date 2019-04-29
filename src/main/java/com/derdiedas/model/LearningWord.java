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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false)
    private boolean isStudied;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    private Word word;
}
