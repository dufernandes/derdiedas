package com.derdiedas.model;

import com.derdiedas.util.WordUtil;
import org.junit.jupiter.api.Test;

import static com.derdiedas.util.WordUtil.verifyWordOnStudyMan;

class WordTest {

    @Test
    void createWordOnStudy_wordExists_returnWordOnStudy() {
        Word mann = WordUtil.createWordMan();
        verifyWordOnStudyMan(mann.createWordOnStudy(true), true);
        verifyWordOnStudyMan(mann.createWordOnStudy(false), false);
    }
}