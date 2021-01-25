package com.currencybot.handlers.impl;

import com.currencybot.config.CallBackStrings;
import com.currencybot.entities.BotState;
import com.currencybot.entities.User;
import com.currencybot.handlers.Handler;
import com.currencybot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserService userService;

    @Autowired
    public StartHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                InlineKeyboardButton.builder().text("доллар").callbackData(CallBackStrings.DOLLAR).build(),
                InlineKeyboardButton.builder().text("евро").callbackData(CallBackStrings.EURO).build(),
                InlineKeyboardButton.builder().text("рубль").callbackData(CallBackStrings.ROUBLE).build());

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));


        SendMessage welcomeMessage = SendMessage.builder()
                .chatId(String.valueOf(user.getId()))
                .text(String.format(
                        "Добрый день, %s \nВыберите валюту, которую будем искать", user.getName()
                ))
                .replyMarkup(inlineKeyboardMarkup)
                .build();

        user.setBotState(BotState.SELECTING_CURRENCY);
        userService.save(user);

        return List.of(welcomeMessage);
    }


    @Override
    public BotState operatedBotState() {
        return BotState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(CallBackStrings.BACK);
    }
}
