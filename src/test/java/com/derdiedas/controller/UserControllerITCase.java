package com.derdiedas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class UserControllerITCase extends BaseUserITCase {

    @WithMockUser("email@email.com") // sets this user as authenticated - note that this user already exists
    @Test
    void findUserByEmail_validEmail_returnUserDto() throws Exception {
        createUser("email@email.com0", "password", "first name0", "last name0");
        createUser("email@email.com1", "password", "first name1", "last name1");
        findUserByEmail("email@email.com0");
    }
}