package pro.sky.telegrambot.handler;

import com.pengrad.telegrambot.model.Update;

public interface Handler {
    void handle(Update update);
}
