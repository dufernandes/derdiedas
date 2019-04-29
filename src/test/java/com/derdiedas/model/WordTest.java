package com.derdiedas.model;

import com.derdiedas.util.WordUtil;
import org.junit.jupiter.api.Test;

import static com.derdiedas.util.WordUtil.verifyLearningWordMan;

class WordTest {

    @Test
    void createLearningWord_wordExists_returnLearningWord() {
        Word mann = WordUtil.createWordMan();
        verifyLearningWordMan(mann.createLearningWord(true), true);
        verifyLearningWordMan(mann.createLearningWord(false), false);
    }
}