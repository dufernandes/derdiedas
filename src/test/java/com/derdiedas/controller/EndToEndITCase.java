package com.derdiedas.controller;

import com.derdiedas.bootstrap.importer.ImporterFromFirstList;
import com.derdiedas.dto.LearningWordDto;
import com.derdiedas.dto.UserDto;
import com.derdiedas.repository.LearningWordRepository;
import com.derdiedas.repository.WordRepository;
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
  private WordRepository wordRepository;

  @Autowired
  private LearningWordRepository learningWordRepository;

  @Test
  void createWords_createUsers_authenticateUser_userLearnWords_assignLearningWordsToUsers() throws Exception {
    importer.doImport();

    UserDto userDto = createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
    String authenticationKey = authenticateUser(EMAIL, PASSWORD);
    Long userId = userDto.getId();

    userDto =
        assignWordsToUserWithAuthKey(userId, EMAIL, FIRST_NAME, LAST_NAME, DIE, ZEIT, DAS, ZIMMER, authenticationKey);
    for (LearningWordDto w : userDto.getWordsStudying()) {
      studyWordWithAuthKey(w.getId(), authenticationKey);
    }

    assignWordsToUserWithAuthKey(userId, EMAIL, FIRST_NAME, LAST_NAME, DIE, TUR, DER, RUCKEN, authenticationKey);
  }
}
