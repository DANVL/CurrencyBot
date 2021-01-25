package com.currencybot.handlers.impl;

import com.currencybot.config.CallBackStrings;
import com.currencybot.entities.BotState;
import com.currencybot.entities.Currency;
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
public class SelectingCurrencyHandler implements Handler {


    private final UserService userService;

    @Autowired
    public SelectingCurrencyHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        switch (message.toLowerCase()) {
            case CallBackStrings.DOLLAR:
                return processSelection(user, Currency.DOLLAR);
            case CallBackStrings.EURO:
                return processSelection(user, Currency.EURO);
            case CallBackStrings.ROUBLE:
                return processSelection(user, Currency.ROUBLE);
            default:
                return Collections.emptyList();
        }

    }

    private List<PartialBotApiMethod<? extends Serializable>> processSelection(User user, Currency currency) {
        user.setSelectedCurrency(currency);
        user.setBotState(BotState.SELECTING_SOURCE);
        userService.save(user);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                InlineKeyboardButton.builder().text("ощад банк").callbackData(CallBackStrings.OSHAD).build(),
                InlineKeyboardButton.builder().text("приват банк").callbackData(CallBackStrings.PRIVAT).build(),
                InlineKeyboardButton.builder().text("https://money24.kharkov.ua/")
                        .callbackData(CallBackStrings.MONEY24).build());

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(user.getId()))
                .text("Выберите источник данных:")
                .replyMarkup(inlineKeyboardMarkup)
                .build();

        return List.of(message);
    }


    @Override
    public BotState operatedBotState() {
        return BotState.SELECTING_CURRENCY;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(CallBackStrings.DOLLAR, CallBackStrings.EURO, CallBackStrings.ROUBLE);
    }
}
