package com.derdiedas.dto;

import com.derdiedas.model.LearningWord;
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
public class LearningWordDto {
    private Long id;
    private WordDto wordDto;
    private boolean isStudied;

    public static LearningWordDto buildFromWord(LearningWord word) {
        ModelMapper modelMapper = new ModelMapper();
        LearningWordDto learningWordDto = null;
        if (word != null) {
            learningWordDto = modelMapper.map(word, LearningWordDto.class);
            if (word.getWord() != null) {
                learningWordDto.setWordDto(WordDto.buildFromWord(word.getWord()));
            }
        }
        return learningWordDto;
    }

    public static Set<LearningWordDto> buildFromWordSet(Set<LearningWord> words) {
        Set<LearningWordDto> dtos = Collections.emptySet();
        if (CollectionUtils.isNotEmpty(words)) {
            dtos = words.stream().map(LearningWordDto::buildFromWord).collect(Collectors.toSet());
        }
        return dtos;
    }
}
