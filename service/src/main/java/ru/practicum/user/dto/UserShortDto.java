package ru.practicum.user.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserShortDto {
    @NotNull
    @NotBlank
    @Size(min = 2,max = 250)
    private String name;
    @NotNull
    @NotBlank
    @Email
    @Size(min = 6,max = 254)
    private String email;
}
