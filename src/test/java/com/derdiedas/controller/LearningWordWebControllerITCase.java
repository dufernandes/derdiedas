package com.derdiedas.controller;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.derdiedas.controller.utils.ApiDocsUtils;
import com.derdiedas.controller.utils.AssertionUtils;
import com.derdiedas.controller.utils.HttpHeadersUtils;
import com.derdiedas.controller.utils.WordUtils;
import com.derdiedas.service.LearningWordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests here only validate the Rest API web layer. The business logic is mocked and tested in other integration tests,
 * or unit tests. Note that here only the {@link LearningWordController} is instantiated, along with the exact classes
 * to be wired. This makes the test execution much faster.
 */
@WebMvcTest(value = LearningWordController.class, secure = false)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import({WordUtils.class, ApiDocsUtils.class, HttpHeadersUtils.class, AssertionUtils.class})
@AutoConfigureRestDocs
class LearningWordWebControllerITCase {

  private static final long LEARNING_WORD_ID = 3L;

  @MockBean
  private LearningWordService learningWordService;

  @Autowired
  private WordUtils wordUtils;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    when(learningWordService.setLearningWordLearnedStatus(anyLong(), anyBoolean())).thenReturn(null);
  }

  @Test
  void updateLearningWordStudied_whenWordIsValidAndIsLearnedParameterIsNotProvided_thenReturnBadRequest()
      throws Exception {
    wordUtils
        .setLearningWordLearnedWithoutStatus(mockMvc, LEARNING_WORD_ID, SpringRestDocs.LearningWordsPage.NO_STATUS_SET,
            SpringRestDocs.LearningWordsPage.NO_STATUS_SET);
  }
}
