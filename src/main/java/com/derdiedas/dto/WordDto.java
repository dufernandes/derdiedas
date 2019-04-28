package com.derdiedas.dto;

import com.derdiedas.model.Word;
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
public class WordDto {
    private Long id;
    private String article;
    private String word;
    private String translation;

    public static WordDto buildFromWord(Word word) {
        ModelMapper modelMapper = new ModelMapper();
        return word != null
                ? modelMapper.map(word, WordDto.class)
                : null;
    }

    public static Set<WordDto> buildFromWordSet(Set<Word> words) {
        Set<WordDto> dtos = Collections.emptySet();
        if (CollectionUtils.isNotEmpty(words)) {
            dtos = words.stream().map(WordDto::buildFromWord).collect(Collectors.toSet());
        }
        return dtos;
    }
}
