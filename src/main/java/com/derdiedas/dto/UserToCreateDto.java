package com.derdiedas.dto;

import com.derdiedas.model.User;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserToCreateDto {

  @Email(message = "Email is not valid")
  @NotBlank(message = "Email must be provided")
  private String email;

  @NotBlank(message = "First Name must be provided")
  private String firstName;

  @NotBlank(message = "Last Name must be provided")
  private String lastName;

  @NotBlank(message = "Password must be provided")
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
