package com.currencybot.utils;

import com.currencybot.entities.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public final class TelegramUtils {

    private TelegramUtils(){}

//    public static SendMessage createMessageTemplate(User user) {
//        return createMessageTemplate(String.valueOf(user.getId()));
//    }
//
//
//// Создаем шаблон SendMessage с включенным Markdown
//
//    public static SendMessage createMessageTemplate(String chatId) {
//        return SendMessage.builder()
//                .chatId(chatId)
//                .;
//    }
//
//
//// Создаем кнопку
//
//    public static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
//        return new InlineKeyboardButton()
//                .setText(text)
//                .setCallbackData(command);
//    }
}
