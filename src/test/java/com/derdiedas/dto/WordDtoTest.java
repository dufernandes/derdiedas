package com.derdiedas.dto;

import com.derdiedas.model.Word;
import com.derdiedas.util.WordUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.derdiedas.util.DtoUtil.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class WordDtoTest {

    @Test
    void buildFromWord_validWord_returnDto() {
        Word man = WordUtil.createWordMan();

        WordDto manDto = WordDto.buildFromWord(man);
        verifyWordDtoMan(manDto);
    }

    @Test
    void buildFromWord_nullParameter_returnNull() {
        assertNull(WordDto.buildFromWord(null));
    }

    @Test
    void buildFromWordSet_validSet_returnSetDto() {
        Word man = WordUtil.createWordMan();
        Word school = WordUtil.createWordSchool();

        Set<WordDto> dtos = WordDto.buildFromWordSet(
                new HashSet<>(asList(man, school)));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        WordDto manDto = dtos.stream()
                .filter(dto -> Objects.equals(dto.getId(), WordUtil.WORD_ID_MAN)).findFirst().orElse(null);
        verifyWordDtoMan(manDto);
        WordDto schoolDto = dtos.stream()
                .filter(dto -> Objects.equals(dto.getId(), WordUtil.WORD_ID_SCHOOL)).findFirst().orElse(null);
        verifyWordDtoSchool(schoolDto);
    }
}