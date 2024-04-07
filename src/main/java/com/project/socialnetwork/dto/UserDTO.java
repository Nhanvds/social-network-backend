package com.project.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    @JsonProperty("email")
    @Email(message = "Email không hợp lệ")
    @NotBlank
    private String email;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    @Size(min=6,max = 25,message = "Password từ 6 đến 25 kí tự")
    private String password;


}
