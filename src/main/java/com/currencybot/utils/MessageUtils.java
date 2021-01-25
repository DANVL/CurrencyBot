package com.currencybot.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.stream.Collectors;

public final class MessageUtils {

    private MessageUtils(){}

    public static SendMessage
    formMessage(String chatId, String messageText, List<List<InlineKeyboardButton>> inlineKeyboard) {

        InlineKeyboardMarkup inlineKeyboardMarkup =
                InlineKeyboardMarkup.builder().keyboard(inlineKeyboard).build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    public static List<InlineKeyboardButton> formKeyboardRow(List<ButtonSpecs> buttonSpecsList){

        return buttonSpecsList.stream()
                .map((button) ->
                        InlineKeyboardButton
                                .builder()
                                .text(button.getText())
                                .callbackData(button.getCommand())
                                .build()).collect(Collectors.toList());
    }
}
