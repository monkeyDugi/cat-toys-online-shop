package com.dallab.cattoy.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SigninRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
