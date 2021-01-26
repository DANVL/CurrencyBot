package com.currencybot.services.impl;

import com.currencybot.config.ConfigStrings;
import com.currencybot.dto.SourceDto;
import com.currencybot.entities.*;
import com.currencybot.exceptions.NoExchangersException;
import com.currencybot.repository.UserRepository;
import com.currencybot.services.CurrencyService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
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

import java.util.List;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
class CurrencyServiceImplTest {

    @Autowired
    private CurrencyService currencyService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void NoExchangersExceptionTest(){
        Mockito.doReturn(null)
                .when(restTemplate)
                .getForObject(ConfigStrings.MONEY24_URI, SourceDto.class);

        Assertions.assertThrows(NoExchangersException.class,
                () -> currencyService.getCurrencyRate(Currency.USD, Source.MONEY24));
    }

    @Test
    public void NoExchangersExceptionTest2(){

        String mock = "-";
        Rate rate = new Rate(mock, mock);
        SourceDto sourceDto = new SourceDto(
                List.of(
                        new Exchanger(1,
                                new Rates(rate, rate, rate))
                )
        );

        Mockito.doReturn(sourceDto)
                .when(restTemplate)
                .getForObject(ConfigStrings.MONEY24_URI, SourceDto.class);

        Assertions.assertThrows(NoExchangersException.class,
                () -> currencyService.getCurrencyRate(Currency.USD, Source.MONEY24));
    }

    @Test
    public void fakeTest(){
        Assert.assertEquals(1,2);
    }
}