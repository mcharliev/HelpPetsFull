package pro.sky.telegrambot.handlers;

import com.pengrad.telegrambot.model.Update;

public interface Handler {
    void handle(Update update);
}
