package com.project.socialnetwork.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @Email(message = "Email không hợp lệ")
    @NotBlank
    private String email;


    private String username;


    @Size(min=6,max = 25,message = "Password từ 6 đến 25 kí tự")
    private String password;


}
