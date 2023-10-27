package com.example.rexcomputantistest.model.user.dto;

public class CreateUserDto {
    private String name;
    private String defaultCurrency;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

}
