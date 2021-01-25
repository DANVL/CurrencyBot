package com.currencybot.handlers.impl;

import com.currencybot.config.ConfigStrings;
import com.currencybot.entities.BotState;
import com.currencybot.entities.User;
import com.currencybot.handlers.Handler;
import com.currencybot.services.UserService;
import com.currencybot.utils.ButtonSpecs;
import com.currencybot.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Component
public class StartHandler implements Handler {

    private final UserService userService;

    @Autowired
    public StartHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        user.setBotState(BotState.SELECTING_CURRENCY);
        userService.save(user);

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne =
                MessageUtils.formKeyboardRow(List.of(
                        new ButtonSpecs("доллар", ConfigStrings.DOLLAR),
                        new ButtonSpecs("евро", ConfigStrings.EURO),
                        new ButtonSpecs("рубль", ConfigStrings.ROUBLE)
                ));

        return List.of(MessageUtils
                .formMessage(
                        String.valueOf(user.getId()),
                        String.format("Добрый день, %s \nВыберите валюту, которую будем искать", user.getName()),
                        List.of(inlineKeyboardButtonsRowOne)));
    }


    @Override
    public BotState operatedBotState() {
        return BotState.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(ConfigStrings.BACK);
    }
}
