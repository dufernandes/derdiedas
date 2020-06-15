package com.derdiedas.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.derdiedas.controller.utils.WordUtils;
import com.derdiedas.model.LearningWord;
import com.derdiedas.model.Word;
import com.derdiedas.util.WordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;

import javax.servlet.ServletContext;

class LearningWordControllerITCase extends BaseITCase {

  @Autowired
  private WordUtils wordUtils;

  @WithMockUser("email@email.com")
  @Test
  void givenWac_whenServletContext_thenItProvidesGreetController() {
    ServletContext servletContext = wac.getServletContext();

    assertNotNull(servletContext);
    assertTrue(servletContext instanceof MockServletContext);
    assertNotNull(wac.getBean("learningWordController"));
  }

  @WithMockUser("email@email.com")
  @Test
  void updateLearningWordStudied_whenWordIsValidAndWordIsLearned_thenCheckUpdatedStatusAsWordLearned() throws Exception {
    Word school = WordUtil.createWordSchoolWithoutId();

    wordRepository.save(school);
    LearningWord schoolLearningWord = school.createLearningWord(false);
    learningWordRepository.save(schoolLearningWord);

    wordUtils.setLearningWordLearnedStatus(getMockMvc(), schoolLearningWord.getId(), true, SpringRestDocs.LearningWordsPage.SET_STATUS_LEARNED);
  }

  @WithMockUser("email@email.com")
  @Test
  void updateLearningWordStudied_whenWordIsValidAndWordIsNotLearned_thenCheckUpdatedStatusAsWordNotLearned() throws Exception {
    Word school = WordUtil.createWordSchoolWithoutId();

    wordRepository.save(school);
    LearningWord schoolLearningWord = school.createLearningWord(true);
    learningWordRepository.save(schoolLearningWord);

    wordUtils.setLearningWordLearnedStatus(getMockMvc(), schoolLearningWord.getId(), false, SpringRestDocs.LearningWordsPage.SET_STATUS_NOT_LEARNED);
  }
}
