package com.currencybot.services.impl;

import com.currencybot.entities.BotState;
import com.currencybot.entities.User;
import com.currencybot.repository.UserRepository;
import com.currencybot.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    public void saveUserTest() {
        User user = User.builder().id(1).name("name").botState(BotState.START).build();

        Mockito.doReturn(user)
                .when(userRepository)
                .save(user);

        User savedUser = userService.save(user);

        Assert.assertEquals(user.getId(), savedUser.getId());
    }

    @Test
    public void saveOrGetSaveTest() {
        User user = User.builder().id(1).name("name").botState(BotState.START).build();

        Mockito.doReturn(user)
                .when(userRepository)
                .save(user);

        Mockito.doReturn(Optional.empty())
                .when(userRepository)
                .findById(1);


        User savedUser = userService.saveOrGetUser(user);

        Assert.assertEquals(user.getId(), savedUser.getId());
    }
}