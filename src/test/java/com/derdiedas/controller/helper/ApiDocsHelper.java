package com.derdiedas.controller.helper;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@Component
public class ApiDocsHelper {

  public MvcResult appendApiDocsIfNecessaryAndReturnMvcResult(String apiDocsId, ResultActions resultActions)
      throws Exception {
    if (isNotEmpty(apiDocsId)) {
      resultActions = resultActions.andDo(document(apiDocsId,
          preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
    }
    return resultActions.andReturn();
  }
}
