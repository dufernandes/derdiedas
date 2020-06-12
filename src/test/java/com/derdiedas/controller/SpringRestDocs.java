package com.derdiedas.controller;

public class SpringRestDocs {
  public static class LoginPage {
    public static final String LOGIN_SUCCESSFUL = "login/login-successful";
    public static final String LOGIN_FAILED = "login/login-failed";
  }

  public static class LearningWordsPage {
    public static final String SET_STATUS = "learning-words/set-status";
  }

  public static class UsersPage {
    public static final String GET_USER_BY_EMAIL = "users/get-user-by-email";
    public static final String GET_USER_BY_ID = "users/get-user-by-id";
    public static final String CREATE_USER = "users/create-user";
    public static final String CREATE_USER_FAILURE = "users/create-user-failure";
    public static final String ASSIGN_LEARNING_WORDS = "users/assign-learning-words";
  }
}
