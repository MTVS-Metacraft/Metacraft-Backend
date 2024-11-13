package com.yogo.metacraft.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    private String id;
    private String password;
}
