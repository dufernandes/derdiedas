package com.derdiedas.controller;

import com.derdiedas.dto.LearningWordDto;
import com.derdiedas.service.LearningWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "learningWords")
public class LearningWordController {

    private final LearningWordService learningWordService;

    @Autowired
    public LearningWordController(LearningWordService learningWordService) {
        this.learningWordService = learningWordService;
    }

    @PutMapping(path = "/{learningWordId}")
    @ResponseBody()
    public LearningWordDto updateLearningWordStudied(@PathVariable("learningWordId") long learningWordId,
                                                     @RequestParam("isStudied") boolean isStudied) {
        return LearningWordDto.buildFromWord(learningWordService.setLearningWordLearnedStatus(learningWordId, isStudied));
    }
}
