package pro.sky.telegrambot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import pro.sky.telegrambot.keyboard.InlineKeyboard;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CallBackQueryHandler implements Handler {
    private final TelegramBot telegramBot;

    public CallBackQueryHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void handle(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        switch (data) {
            case "Кнопка 1": {
                InlineKeyboard infoKeyboard = new InlineKeyboard(telegramBot);
                infoKeyboard.showInfoShelterMenu(chatId);
                break;
            }
            case "Кнопка 2": {
                sendTextMessage(chatId, "Нажата кнопка 2");
                break;
            }
            case "Кнопка 3": {
                sendTextMessage(chatId, "Нажата кнопка 3");
                break;
            }
            case "Кнопка 4": {
                sendTextMessage(chatId, "Нажата кнопка 4");
                break;
            }
            case "Кнопка 1.1": {
                showInfoAboutShelter(chatId);
                break;
            }
            case "Кнопка 1.2": {
                showContactInfoAboutShelter(chatId);
                break;
            }
            case "Кнопка 1.3": {
                showSafetyAdvice(chatId);
                break;
            }
        }
    }

    private void sendTextMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        telegramBot.execute(sendMessage);
    }

    private void showInfoAboutShelter(Long chatId) {
        sendTextMessage(chatId, "Приют Help Pets это место содержания бездомных, потерянных, брошенных " +
                "и больных животных.Тут находятся питомцы, от которых отказались хозяева, найденные на улице " +
                "раненые кошки и собаки. Основаные функции приюта это:" +
                "\nпринимать животных от владельцев или найденных на улице;" +
                "\nсоздать хорошие условия для проживания;" +
                "\nпроводить работу по поиску новых хозяев;" +
                "\nвременно принять животных, сданных владельцами;" +
                "\nприютить больных или травмированных кошек и собак.");
    }

    private void showContactInfoAboutShelter(Long chatId) {
        try {
            byte[] drivingDirection = Files.readAllBytes(
                    Paths.get(CallBackQueryHandler.class.getResource("/drivingDirection.jpg").toURI()));
            SendPhoto sendPhoto = new SendPhoto(chatId, drivingDirection);
            sendTextMessage(chatId, "Часы работы приюта Help Pets с 9:00 до 19:00 без выходных," +
                    " приют расположен по адресу: Зубовский бульвар д.17 с.3");
            telegramBot.execute(sendPhoto);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void showSafetyAdvice(Long chatId) {
        sendTextMessage(chatId, "На территории Приюта для всех посетителей действуют правила и распорядок," +
                " установленные администрацией Приюта: " +
                "\nпроведение фото и видео фиксации без предварительного письменного согласования;" +
                "\nкормить животных кормами и продуктами, как на территории Приюта,;" +
                "\nпосещать блок карантина и изолятор;" +
                "\nбез необходимости находиться вблизи вольеров;" +
                "\nдавать животным самостоятельно какие-либо ветеринарные или медицинские препараты." +
                "\n выгуливать животных без поводка .");
    }
}
