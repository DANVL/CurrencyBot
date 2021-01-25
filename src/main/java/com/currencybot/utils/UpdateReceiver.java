package com.currencybot.utils;

import com.currencybot.entities.BotState;
import com.currencybot.entities.User;
import com.currencybot.handlers.Handler;
import com.currencybot.services.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
public class UpdateReceiver {

    private final UserService userService;

    private final List<Handler> handlers;


    public UpdateReceiver(UserService userService, List<Handler> handlers) {
        this.userService = userService;
        this.handlers = handlers;
    }

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            if (isMessageWithText(update)) {
                Message message = update.getMessage();

                User user = getUser(message.getFrom());

                return getHandlerByState(user.getBotState()).handle(user, message.getText());

            } else if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();

                return getHandlerByCallBackQuery(callbackQuery.getData())
                        .handle(getUser(callbackQuery.getFrom()), callbackQuery.getData());
            }

            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }
    }

    private User getUser(org.telegram.telegrambots.meta.api.objects.User form){
        int id = form.getId();
        String name = form.getFirstName();
        String surname = form.getLastName();

        User user = User.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .botState(BotState.START)
                .build();


        return userService.saveOrGetUser(user);
    }

    private Handler getHandlerByState(BotState state) {
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().equals(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        return handlers.stream()
                .filter(h -> h.operatedCallBackQuery().stream()
                        .anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
