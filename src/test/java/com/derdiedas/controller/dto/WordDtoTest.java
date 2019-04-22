package com.derdiedas.controller.dto;

import com.derdiedas.model.Word;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.derdiedas.controller.dto.DtoUtil.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class WordDtoTest {

    @Test
    void buildFromWord_validWord_returnDto() {
        Word man = createWordMan();

        WordDto manDto = WordDto.buildFromWord(man);
        verifyWordDtoMan(manDto);
    }

    @Test
    void buildFromWord_nullParameter_returnNull() {
        assertNull(WordDto.buildFromWord(null));
    }

    @Test
    void buildFromWordSet_validSet_returnSetDto() {
        Word man = createWordMan();
        Word school = createWordSchool();

        Set<WordDto> dtos = WordDto.buildFromWordSet(
                new HashSet<>(asList(man, school)));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        WordDto manDto = dtos.stream()
                .filter(dto -> Objects.equals(dto.getId(), WORD_ID_MAN)).findFirst().orElse(null);
        verifyWordDtoMan(manDto);
        WordDto schoolDto = dtos.stream()
                .filter(dto -> Objects.equals(dto.getId(), WORD_ID_SCHOOL)).findFirst().orElse(null);
        verifyWordDtoSchool(schoolDto);
    }
}