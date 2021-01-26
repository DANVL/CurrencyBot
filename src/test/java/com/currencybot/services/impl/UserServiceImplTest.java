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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.properties")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@TestPropertySource(locations = "classpath:test.properties")
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

    @Test
    public void saveOrGetGetTest() {
        User user = User.builder().id(1).name("name").botState(BotState.START).build();

        Mockito.doReturn(user)
                .when(userRepository)
                .save(user);

        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(1);


        User savedUser = userService.saveOrGetUser(user);

        Assert.assertEquals(user.getId(), savedUser.getId());
    }
}