package com.example.rexcomputantistest.testData;

import com.example.rexcomputantistest.model.user.User;
import org.springframework.stereotype.Service;


@Service
public class TestDataUserService {
    public User getRandomUser() {

        User newUser = new User();

        newUser.setName("Username_" + Math.random()*100);
        newUser.setDefaultCurrency("RUB");

        return newUser;
    }
}
