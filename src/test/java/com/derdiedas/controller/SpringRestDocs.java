package com.derdiedas.controller;

public class SpringRestDocs {
  public static class LoginPage {
    public static final String LOGIN_SUCCESSFUL = "login/login-successful";
    public static final String LOGIN_FAILED = "login/login-failed";
  }

  public static class LearningWordsPage {
    public static final String SET_STATUS_LEARNED = "learning-words/set-status-learned";
    public static final String SET_STATUS_NOT_LEARNED = "learning-words/set-status-not-learned";
    public static final String NO_STATUS_SET = "learning-words/no-status-set";
  }

  public static class UsersPage {
    public static final String GET_USER_BY_EMAIL = "users/get-user-by-email";
    public static final String GET_USER_BY_ID = "users/get-user-by-id";
    public static final String CREATE_USER = "users/create-user";
    public static final String CREATE_USER_FAILURE = "users/create-user-failure";
    public static final String ASSIGN_LEARNING_WORDS = "users/assign-learning-words";
  }

  public static class SampleFlowPage {
    public static final String CREATE_USER = "sample-flow/create-user";
    public static final String LOGIN = "sample-flow/login";
    public static final String ASSIGN_WORDS_TO_THE_USER = "sample-flow/assign-words-to-the-user";
    public static final String LEARN_FIRST_WORD = "sample-flow/learn-first-word";
    public static final String DO_NOT_LEARN_SECOND_WORD = "sample-flow/do-not-learn-second-word";
    public static final String LEARN_THIRD_WORD = "sample-flow/learn-third-word";
    public static final String FETCH_USER_WITH_LEARNING_WORDS = "sample-flow/fetch-users-with-learning-words";
  }
}

