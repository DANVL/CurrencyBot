package com.currencybot.utils;

import com.currencybot.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
class MessageUtilsTest {

    @MockBean
    UserRepository userRepository;

    @Test
    public void formMessageTest() {
        String chatId = "1";
        String text = "text";

        List<InlineKeyboardButton> inlineKeyboardButtons =
                List.of(
                        InlineKeyboardButton
                                .builder()
                                .text("")
                                .callbackData("")
                                .build()
                );
        InlineKeyboardMarkup inlineKeyboardMarkup =
                InlineKeyboardMarkup.builder().keyboard(List.of(inlineKeyboardButtons)).build();

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();

        SendMessage generatedMessage = MessageUtils.formMessage(chatId, text, List.of(inlineKeyboardButtons));

        Assert.assertEquals(sendMessage, generatedMessage);
    }

    @Test
    public void formKeyboardRowTest() {
        String text = "text";
        String callbackData = "callbackData";

        List<InlineKeyboardButton> inlineKeyboardButtons =
                List.of(
                        InlineKeyboardButton
                                .builder()
                                .text(text)
                                .callbackData(callbackData)
                                .build()
                );

        List<InlineKeyboardButton> formedInlineKeyboardButtons =
                MessageUtils.formKeyboardRow(List.of(
                        new ButtonSpecs(text, callbackData)
                ));

        Assert.assertEquals(inlineKeyboardButtons, formedInlineKeyboardButtons);
    }
}