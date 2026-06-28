package com.vacationable.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Email
    @NotBlank
    String email;

    @NotBlank
    @Size(min = 6)
    String password;

    @Size(max = 100)
    String firstName;

    @Size(max = 100)
    String lastName;
}
