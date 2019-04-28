package com.derdiedas.dto;

import com.derdiedas.model.User;
import lombok.*;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserToCreateDto {

    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public static User toUser(UserToCreateDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        User user = null;
        if (userDto != null) {
            user = modelMapper.map(userDto, User.class);
        }
        return user;
    }
}
