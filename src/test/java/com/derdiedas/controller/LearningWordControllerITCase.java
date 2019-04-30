package com.derdiedas.controller;

import com.derdiedas.dto.LearningWordDto;
import com.derdiedas.model.LearningWord;
import com.derdiedas.model.Word;
import com.derdiedas.repository.LearningWordRepository;
import com.derdiedas.repository.WordRepository;
import com.derdiedas.util.WordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LearningWordControllerITCase extends BaseUserITCase {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private LearningWordRepository learningWordRepository;

    @Autowired
    private WordRepository wordRepository;

    @WithMockUser("email@email.com") // sets this user as authenticated - note that this user already exists
    @Test
    void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("learningWordController"));
    }

    @WithMockUser("email@email.com") // sets this user as authenticated - note that this user already exists
    @Test
    void updateLearningWordStudied_validWord_checkUpdatedStatus() throws Exception {
        learningWordRepository.deleteAll();
        wordRepository.deleteAll();

        Word school = WordUtil.createWordSchoolWithoutId();

        wordRepository.save(school);
        LearningWord schoolLearningWord = school.createLearningWord(false);
        learningWordRepository.save(schoolLearningWord);

        MvcResult mvcResult = this.mockMvc
                .perform(put("/learningWords/" + schoolLearningWord.getId() + "?isStudied=" + Boolean.TRUE.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andDo(document("learning-words/set-status",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        LearningWordDto result = objectMapper.readValue(contentAsString, LearningWordDto.class);

        assertNotNull(result);
        assertTrue(result.isStudied());
    }
}
