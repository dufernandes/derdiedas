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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false)
    private String article;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    private String translation;

    @Singular
    @OneToMany(
            mappedBy = "word",
            cascade = CascadeType.ALL
    )
    @EqualsAndHashCode.Exclude
    private Set<LearningWord> learningWords;

    public LearningWord createLearningWord(boolean isStudied) {
        return LearningWord.builder()
                .isStudied(isStudied)
                .word(this)
                .build();
    }
}
