package com.derdiedas.controller.dto;

import com.derdiedas.model.Word;
import com.derdiedas.model.WordOnStudy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class WordOnStudyDto {
    private Long id;
    private WordDto wordDto;
    private boolean isStudied;

    public static WordOnStudyDto buildFromWord(WordOnStudy word) {
        ModelMapper modelMapper = new ModelMapper();
        WordOnStudyDto wordOnStudyDto = null;
        if (word != null) {
            wordOnStudyDto = modelMapper.map(word, WordOnStudyDto.class);
            if (word.getWord() != null) {
                wordOnStudyDto.setWordDto(WordDto.buildFromWord(word.getWord()));
            }
        }
        return wordOnStudyDto;
    }

    public static Set<WordOnStudyDto> buildFromWordSet(Set<WordOnStudy> words) {
        Set<WordOnStudyDto> dtos = Collections.emptySet();
        if (CollectionUtils.isNotEmpty(words)) {
            dtos = words.stream().map(WordOnStudyDto::buildFromWord).collect(Collectors.toSet());
        }
        return dtos;
    }
}
