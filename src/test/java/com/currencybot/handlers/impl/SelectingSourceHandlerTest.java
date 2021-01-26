package com.currencybot.handlers.impl;

import com.currencybot.config.ConfigStrings;
import com.currencybot.dto.SourceDto;
import com.currencybot.entities.*;
import com.currencybot.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
class SelectingSourceHandlerTest {

    @Autowired
    SelectingSourceHandler selectingSourceHandler;

    @MockBean
    UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;


    @Test
    public void handleTest() {
        User user = User.builder()
                .id(1).name("name").botState(BotState.START)
                .selectedCurrency(Currency.USD).selectedSource(Source.OSHAD)
                .build();

        Mockito.doReturn(user)
                .when(userRepository)
                .save(user);

        String mock = "-";
        Rate rate = new Rate(mock, mock);
        SourceDto sourceDto = new SourceDto(
                List.of(
                        new Exchanger(9,
                                new Rates(rate, rate, rate)),
                        new Exchanger(13,
                                new Rates(rate, rate, rate)),
                        new Exchanger(25,
                                new Rates(rate, rate, rate))
                )
        );

        Mockito.doReturn(sourceDto)
                .when(restTemplate)
                .getForObject(ConfigStrings.BANKS_URI, SourceDto.class);

        List<PartialBotApiMethod<? extends Serializable>> handle1 =
                selectingSourceHandler.handle(user, ConfigStrings.OSHAD);

        List<PartialBotApiMethod<? extends Serializable>> handle2 =
                selectingSourceHandler.handle(user, "");

        Assert.assertTrue(handle1.size() > 0);

        Assert.assertEquals(0, handle2.size());
    }

    @Test
    public void operatedBotStateTest(){
        BotState botState = BotState.SELECTING_SOURCE;
        BotState returnBotState = selectingSourceHandler.operatedBotState();

        Assert.assertEquals(botState,returnBotState);
    }

    @Test
    public void operatedCallbackQueryTest(){
        List<String> operatedCallbackQueries =
                List.of(ConfigStrings.OSHAD, ConfigStrings.PRIVAT, ConfigStrings.MONEY24);
        List<String> returnedOperatedCallbackQueries = selectingSourceHandler.operatedCallBackQuery();

        Assert.assertEquals(operatedCallbackQueries,returnedOperatedCallbackQueries);

    }
}