package com.derdiedas.controller.dto;

import com.derdiedas.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    public static UserDto buildFromUser(User user) {
        return user != null
                ? new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName())
                : null;
    }

    public static List<UserDto> buildListFromUsers(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(users)) {
            users.forEach(user -> dtos.add(buildFromUser(user)));
        }

        return dtos;
    }
}
