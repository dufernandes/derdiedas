package com.derdiedas.controller;

import com.derdiedas.dto.UserDto;
import com.derdiedas.dto.UserToCreateDto;
import com.derdiedas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.derdiedas.controller.QueryStringConstants.*;

@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @ResponseBody
    public UserDto findUserByIdOrEmail(@RequestParam("fetchType") String fetchType, @RequestParam("idOrEmail") String idOrEmail) {
        UserDto userDto;
        switch (fetchType) {
            case FETCH_TYPE_EMAIL:
                userDto = UserDto
                        .buildFromUser(userService
                                .findUserByEmail(idOrEmail
                                ).orElseThrow(() -> new IllegalArgumentException("email does not represent any registered user")));
                break;
            case FETCH_TYPE_ID:
                userDto = UserDto
                        .buildFromUser(userService
                                .findUserById(Long.parseLong(idOrEmail)
                                ).orElseThrow(() -> new IllegalArgumentException("id does not represent any registered user")));
                break;
            default:
                throw new IllegalArgumentException("fetchType does not represent any implemented type");

        }
        return userDto;
    }

    @PostMapping()
    @ResponseBody()
    public UserDto create(@Valid @RequestBody UserToCreateDto user) {
        return UserDto.buildFromUser(userService.createUser(user));
    }

    @PutMapping(path = "/{userId}")
    @ResponseBody()
    public UserDto assignLearningWordsToUser(@PathVariable("userId") long userId,
                                             @RequestParam("action") String action) {
        if (!ACTION_ASSIGN_LEARNING_WORDS.equals(action)) {
            throw new IllegalArgumentException("there must be a query string called action with value " + ACTION_ASSIGN_LEARNING_WORDS);
        }
        return UserDto.buildFromUser(userService.assignLearningWordsToUser(userId));
    }
}
