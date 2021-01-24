package com.currencybot.handlers;

import com.currencybot.bot.BotState;
import com.currencybot.entities.User;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import java.io.Serializable;
import java.util.List;

public interface Handler {
    List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message);

    BotState operatedBotState();

    List<String> operatedCallBackQuery();
}
