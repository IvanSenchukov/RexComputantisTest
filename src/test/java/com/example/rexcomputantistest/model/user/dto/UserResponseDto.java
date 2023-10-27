package com.example.rexcomputantistest.model.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResponseDto {

    private Long id;
    private String name;
    private String defaultCurrency;
}
