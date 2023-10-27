package com.example.rexcomputantistest.model.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateUserDto {

    private String name;
    private String defaultCurrency;
}
