package com.derdiedas.dto;

import com.derdiedas.model.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private int wordsPerGroup;
    private int studyGroupPage;
    private Set<LearningWordDto> wordsStudying = new HashSet<>();

    public static UserDto buildFromUser(User user) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto dto = null;
        if (user != null) {
            dto = modelMapper.map(user, UserDto.class);
            dto.setWordsStudying(LearningWordDto.buildFromWordSet(user.getLearningWords()));
        }
        return dto;
    }

    public static List<UserDto> buildListFromUsers(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(users)) {
            users.forEach(user -> dtos.add(buildFromUser(user)));
        }

        return dtos;
    }
}
