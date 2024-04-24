package com.project.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @Email(message = "Email không hợp lệ")
    @NotBlank
    private String email;


    private String username;


    @Size(min=6,max = 25,message = "Password từ 6 đến 25 kí tự")
    private String password;


}
