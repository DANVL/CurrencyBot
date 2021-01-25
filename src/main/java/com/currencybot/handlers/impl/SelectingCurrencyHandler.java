package com.currencybot.handlers.impl;

import com.currencybot.config.ConfigStrings;
import com.currencybot.entities.BotState;
import com.currencybot.entities.Currency;
import com.currencybot.entities.User;
import com.currencybot.handlers.Handler;
import com.currencybot.services.UserService;
import com.currencybot.utils.ButtonSpecs;
import com.currencybot.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
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
            case ConfigStrings.DOLLAR:
                return processSelection(user, Currency.usd);
            case ConfigStrings.EURO:
                return processSelection(user, Currency.eur);
            case ConfigStrings.ROUBLE:
                return processSelection(user, Currency.rur);
            default:
                return Collections.emptyList();
        }

    }

    private List<PartialBotApiMethod<? extends Serializable>> processSelection(User user, Currency currency) {
        user.setSelectedCurrency(currency);
        user.setBotState(BotState.SELECTING_SOURCE);
        userService.save(user);


        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = MessageUtils.formKeyboardRow(List.of(
                new ButtonSpecs("ощад банк",ConfigStrings.OSHAD),
                new ButtonSpecs("приват банк",ConfigStrings.PRIVAT),
                new ButtonSpecs("https://money24.kharkov.ua/",ConfigStrings.MONEY24)
        ));


        return List.of(MessageUtils.formMessage(
                String.valueOf(user.getId()),
                "Выберите источник данных:",
                List.of(inlineKeyboardButtonsRowOne)
        ));
    }


    @Override
    public BotState operatedBotState() {
        return BotState.SELECTING_CURRENCY;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(ConfigStrings.DOLLAR, ConfigStrings.EURO, ConfigStrings.ROUBLE);
    }
}
