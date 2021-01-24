package com.currencybot.handlers;

import com.currencybot.bot.BotState;
import com.currencybot.entities.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
public class StartHandler implements Handler {

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                InlineKeyboardButton.builder().text("доллар").callbackData("/dollar").build(),
                InlineKeyboardButton.builder().text("евро").callbackData("/euro").build(),
                InlineKeyboardButton.builder().text("рубль").callbackData("/rouble").build());

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));


        SendMessage welcomeMessage = SendMessage.builder()
                .chatId(String.valueOf(user.getId()))
                .text(String.format(
                        "Добрый день, %s \nВыберите валюту, которую будем искать", user.getName()
                ))
                .replyMarkup(inlineKeyboardMarkup)
                .build();

        return List.of(welcomeMessage);
    }

    @Override
    public BotState operatedBotState() {
        return BotState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
