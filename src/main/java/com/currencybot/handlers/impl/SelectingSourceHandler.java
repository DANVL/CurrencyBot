package com.currencybot.handlers.impl;

import com.currencybot.config.ConfigStrings;
import com.currencybot.entities.BotState;
import com.currencybot.entities.Rate;
import com.currencybot.entities.Source;
import com.currencybot.entities.User;
import com.currencybot.exceptions.NoExchangersException;
import com.currencybot.exceptions.NotSupportedCurrencyException;
import com.currencybot.handlers.Handler;
import com.currencybot.services.CurrencyService;
import com.currencybot.services.UserService;
import com.currencybot.utils.ButtonSpecs;
import com.currencybot.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


@Slf4j
@Component
public class SelectingSourceHandler implements Handler {

    private final UserService userService;
    private final CurrencyService currencyService;

    @Autowired
    public SelectingSourceHandler(UserService userService, CurrencyService currencyService) {
        this.userService = userService;
        this.currencyService = currencyService;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {

        try {
            switch (message.toLowerCase()) {
                case ConfigStrings.OSHAD:
                    return processSelection(user, Source.OSHAD);
                case ConfigStrings.PRIVAT:
                    return processSelection(user, Source.PRIVAT);
                case ConfigStrings.MONEY24:
                    return processSelection(user, Source.MONEY24);
                default:
                    return Collections.emptyList();
            }
        } catch (NoExchangersException e) {
            log.error("Exchangers not found");
            return Collections.emptyList();
        } catch (NotSupportedCurrencyException e) {
            log.error("Not supported currency found");
            return Collections.emptyList();
        }

    }

    private List<PartialBotApiMethod<? extends Serializable>> processSelection(User user, Source source)
            throws NotSupportedCurrencyException, NoExchangersException {

        user.setSelectedSource(source);
        user.setBotState(BotState.START);
        userService.save(user);

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = MessageUtils.formKeyboardRow(List.of(
                new ButtonSpecs("Выбрать заново", ConfigStrings.BACK)
        ));

        Rate rate = currencyService.getCurrencyRate(user.getSelectedCurrency(), user.getSelectedSource());

        return List.of(MessageUtils.formMessage(
                String.valueOf(user.getId()),
                String.format(
                        "Данные о курсе валюты:\n\nпокупка\\продажа(%s):\n%s\\%s",
                        user.getSelectedCurrency(), rate.getBuy(), rate.getSel()
                ),
                List.of(inlineKeyboardButtonsRowOne)
        ));
    }


    @Override
    public BotState operatedBotState() {
        return BotState.SELECTING_SOURCE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(ConfigStrings.OSHAD, ConfigStrings.PRIVAT, ConfigStrings.MONEY24);
    }
}
