package com.derdiedas.controller.dto;

import com.derdiedas.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserToCreateDto {

    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public static User toUser(UserToCreateDto userDto) {
        User user = null;
        if (userDto != null) {
            user = new User();
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
        }
        return user;
    }
}
