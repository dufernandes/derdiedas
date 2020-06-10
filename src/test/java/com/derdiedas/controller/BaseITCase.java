package com.derdiedas.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.derdiedas.repository.LearningWordRepository;
import com.derdiedas.repository.UserRepository;
import com.derdiedas.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.AccessLevel;
import lombok.Getter;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class BaseITCase {

  @Getter(AccessLevel.PROTECTED)
  private MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext wac;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected LearningWordRepository learningWordRepository;

  @Autowired
  protected WordRepository wordRepository;

  @BeforeEach
  public void setUp(WebApplicationContext webApplicationContext,
                    RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(springSecurity()) // enables security for testing
        .apply(documentationConfiguration(restDocumentation))
        .build();
    userRepository.deleteAll();
    learningWordRepository.deleteAll();
    wordRepository.deleteAll();
  }
}
