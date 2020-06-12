package com.derdiedas.controller;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.derdiedas.bootstrap.importer.ImporterFromFirstList;
import com.derdiedas.controller.utils.UserAuthenticationUtils;
import com.derdiedas.controller.utils.UserUtils;
import com.derdiedas.controller.utils.WordUtils;
import com.derdiedas.dto.LearningWordDto;
import com.derdiedas.dto.UserDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EndToEndITCase extends BaseITCase {

  private static final String EMAIL = "emailAA@email.com0";
  private static final String PASSWORD = "passwordAA";
  private static final String FIRST_NAME = "firstAA name0";
  private static final String LAST_NAME = "lastAA name0";

  private static final String DIE = "die";
  private static final String ZEIT = "Zeit";
  private static final String DAS = "das";
  private static final String ZIMMER = "Zimmer";

  private static final String TUR = "Tür";
  private static final String DER = "der";
  private static final String RUCKEN = "Rücken";

  @Autowired
  private ImporterFromFirstList importer;

  @Autowired
  private UserAuthenticationUtils userAuthenticationUtils;

  @Autowired
  private WordUtils wordUtils;

  @Autowired
  private UserUtils userUtils;

  @Test
  void createWords_createUsers_authenticateUser_userLearnWords_assignLearningWordsToUsers_verificationMadeForWordsAndArticles()
      throws Exception {
    importer.doImport();

    UserDto userDto = userUtils.createUserValidatingAndGettingResponse(getMockMvc(), EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
    String authenticationKey = userAuthenticationUtils.authenticateUser(getMockMvc(), EMAIL, PASSWORD);
    Long userId = userDto.getId();

    userDto =
        userUtils
            .assignWordsToUserWithAuthKey(getMockMvc(), userId, EMAIL, FIRST_NAME, LAST_NAME, DIE, ZEIT, DAS, ZIMMER,
                authenticationKey);
    for (LearningWordDto w : userDto.getWordsStudying()) {
      wordUtils.studyWordWithAuthKey(getMockMvc(), w.getId(), authenticationKey);
    }

    userUtils.assignWordsToUserWithAuthKey(getMockMvc(), userId, EMAIL, FIRST_NAME, LAST_NAME, DIE, TUR, DER, RUCKEN,
        authenticationKey);
  }

  @Disabled(value = "Test takes to long to run, thus it is disabled")
  @Test
  void userLearnAllWords()
      throws Exception {
    importer.doImport();

    UserDto userDto = userUtils.createUserValidatingAndGettingResponse(getMockMvc(), EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
    String authenticationKey = userAuthenticationUtils.authenticateUser(getMockMvc(), EMAIL, PASSWORD);
    Long userId = userDto.getId();

    userDto =
        userUtils.assignWordsToUser(getMockMvc(), userId, authenticationKey);
    int studyPage = userDto.getStudyGroupPage();
    while (isNotEmpty(userDto.getWordsStudying())) {
      for (LearningWordDto w : userDto.getWordsStudying()) {
        wordUtils.studyWordWithAuthKey(getMockMvc(), w.getId(), authenticationKey);
      }

      userDto = userUtils.assignWordsToUser(getMockMvc(), userId, authenticationKey);
      assertNotEquals(studyPage, userDto.getStudyGroupPage());
      studyPage = userDto.getStudyGroupPage();
    }
  }
}
