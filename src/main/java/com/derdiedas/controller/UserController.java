package com.derdiedas.controller;

import com.derdiedas.controller.dto.UserDto;
import com.derdiedas.controller.dto.UserToCreateDto;
import com.derdiedas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @GetMapping()
    @ResponseBody
    public UserDto findUserByEmail(@RequestParam("email") String email) {
        return UserDto.buildFromUser(userService.findByEmail(email));
    }

    @PostMapping()
    @ResponseBody()
    public UserDto create(@Valid @RequestBody UserToCreateDto user) {
        return UserDto.buildFromUser(userService.save(UserToCreateDto.toUser(user)));
    }
}
