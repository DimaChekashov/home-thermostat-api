package com.example.home_thermostat_api.telegram;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class TelegramKeyboards {
    public ReplyKeyboardMarkup getAuthKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("🔐 Войти"));
        row1.add(new KeyboardButton("📝 Регистрация"));
        rows.add(row1);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    public ReplyKeyboardMarkup getMainKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("🏠 Мои дома"));
        row1.add(new KeyboardButton("🌡️ Температура"));
        rows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("📖 Инструкция"));
        row2.add(new KeyboardButton("🚪 Выйти"));
        rows.add(row2);

        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
