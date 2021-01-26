package com.currencybot.handlers.impl;

import com.currencybot.config.ConfigStrings;
import com.currencybot.entities.BotState;
import com.currencybot.entities.User;
import com.currencybot.repository.UserRepository;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
class SelectingCurrencyHandlerTest {

    @Autowired
    SelectingCurrencyHandler selectingCurrencyHandler;

    @MockBean
    UserRepository userRepository;

    @Test
    public void handleTest() {
        User user = User.builder().id(1).name("name").botState(BotState.START).build();

        Mockito.doReturn(user)
                .when(userRepository)
                .save(user);

        List<PartialBotApiMethod<? extends Serializable>> handle1 =
                selectingCurrencyHandler.handle(user, ConfigStrings.DOLLAR);
        List<PartialBotApiMethod<? extends Serializable>> handle2 =
                selectingCurrencyHandler.handle(user, ConfigStrings.EURO);
        List<PartialBotApiMethod<? extends Serializable>> handle3 =
                selectingCurrencyHandler.handle(user, ConfigStrings.ROUBLE);

        List<PartialBotApiMethod<? extends Serializable>> handle4 =
                selectingCurrencyHandler.handle(user, "");

        Assert.assertTrue(handle1.size() > 0);
        Assert.assertTrue(handle2.size() > 0);
        Assert.assertTrue(handle3.size() > 0);

        Assert.assertEquals(0, handle4.size());
    }

    @Test
    public void operatedBotStateTest(){
        BotState botState = BotState.SELECTING_CURRENCY;
        BotState returnBotState = selectingCurrencyHandler.operatedBotState();

        Assert.assertEquals(botState,returnBotState);
    }

    @Test
    public void operatedCallbackQueryTest(){
        List<String> operatedCallbackQueries = List.of(ConfigStrings.DOLLAR, ConfigStrings.EURO, ConfigStrings.ROUBLE);
        List<String> returnedOperatedCallbackQueries = selectingCurrencyHandler.operatedCallBackQuery();

        Assert.assertEquals(operatedCallbackQueries,returnedOperatedCallbackQueries);

    }

}