package com.currencybot.handlers.impl;

import com.currencybot.config.CallBackStrings;
import com.currencybot.entities.BotState;
import com.currencybot.entities.Source;
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
public class SelectingSourceHandler implements Handler {

    private final UserService userService;

    @Autowired
    public SelectingSourceHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        switch (message.toLowerCase()) {
            case CallBackStrings.OSHAD:
                return processSelection(user, Source.OSHAD);
            case CallBackStrings.PRIVAT:
                return processSelection(user, Source.PRIVAT);
            case CallBackStrings.MONEY24:
                return processSelection(user, Source.MONEY24);
            default:
                return Collections.emptyList();
        }
    }

    private List<PartialBotApiMethod<? extends Serializable>> processSelection(User user, Source source) {
        user.setSelectedSource(source);
        user.setBotState(BotState.START);
        userService.save(user);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                InlineKeyboardButton.builder().text("Выбрать заново").callbackData(CallBackStrings.BACK).build());

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(user.getId()))
                .text("Данные по валюте:")
                .replyMarkup(inlineKeyboardMarkup)
                .build();

        return List.of(message);
    }

    @Override
    public BotState operatedBotState() {
        return BotState.SELECTING_SOURCE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(CallBackStrings.OSHAD, CallBackStrings.PRIVAT, CallBackStrings.MONEY24);
    }
}
