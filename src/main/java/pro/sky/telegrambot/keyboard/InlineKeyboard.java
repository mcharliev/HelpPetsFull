package pro.sky.telegrambot.keyboard;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {
    private final TelegramBot telegramBot;

    public InlineKeyboard(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void showStartMenu(Long chatId) {
        String text = "Привет," +
                " Вас приветствует помощник приложения Help-Pets," +
                " пожалуйста выберите пункт из представленного меню ";
        InlineKeyboardButton firstButton = new InlineKeyboardButton("Узнать информацию о приюте");
        firstButton.callbackData("Кнопка 1");
        InlineKeyboardButton secondButton = new InlineKeyboardButton("Как взять собаку из приюта");
        secondButton.callbackData("Кнопка 2");
        InlineKeyboardButton thirdButton = new InlineKeyboardButton("Прислать отчет о питомце");
        thirdButton.callbackData("Кнопка 3");
        InlineKeyboardButton fourthButton = new InlineKeyboardButton("Позвать волонтера");
        fourthButton.callbackData("Кнопка 4");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(firstButton, secondButton);
        inlineKeyboardMarkup.addRow(thirdButton, fourthButton);
        SendMessage sendMessage = new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(sendMessage);
    }

    public void showInfoShelterMenu(Long chatId) {
        String text = " Вы зашли в раздел  информации о приюте," +
                " пожалуйста выберите пункт из представленного меню ";
        InlineKeyboardButton firstButton = new InlineKeyboardButton("Подробная информация о приюте");
        firstButton.callbackData("Кнопка 1.1");
        InlineKeyboardButton secondButton = new InlineKeyboardButton("Контактая информация");
        secondButton.callbackData("Кнопка 1.2");
        InlineKeyboardButton thirdButton = new InlineKeyboardButton("Рекомендации" +
                " о технике безопасности на территории приюта");
        thirdButton.callbackData("Кнопка 1.3");
        InlineKeyboardButton fourthButton = new InlineKeyboardButton("Записать " +
                "контактные данные для связи");
        fourthButton.callbackData("Кнопка 1.4");
        InlineKeyboardButton fifthButton = new InlineKeyboardButton("Позвать волонтера");
        fifthButton.callbackData("Кнопка 1.5");
        InlineKeyboardButton sixthButton = new InlineKeyboardButton("Вернуться на главное меню");
        sixthButton.callbackData("Кнопка 1.6");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(firstButton, secondButton);
        inlineKeyboardMarkup.addRow(thirdButton, fourthButton);
        inlineKeyboardMarkup.addRow(fifthButton, sixthButton);
        SendMessage sendMessage = new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(sendMessage);
    }

    public void showBureaucraticMenu(Long chatId) {
        String text = " Вы зашли в раздел бюрократической информации," +
                " и бытовых вопросов, даннгый раздел поможет Вам получить полную информацию" +
                " о том, как предстоит подготовиться человеку ко встрече с новым членом семьи.";
        InlineKeyboardButton firstButton = new InlineKeyboardButton("Правила знакомства с собакой");
        firstButton.callbackData("Кнопка 2.1");
        InlineKeyboardButton secondButton = new InlineKeyboardButton("Присок документов, чтобы взять собаку");
        secondButton.callbackData("Кнопка 2.2");
        InlineKeyboardButton thirdButton = new InlineKeyboardButton("Рекомендации по транспортировке");
        thirdButton.callbackData("Кнопка 2.3");
        InlineKeyboardButton fourthButton = new InlineKeyboardButton("Рекомендации по обустройству дома" +
                " для щенка");
        fourthButton.callbackData("Кнопка 2.4");
        InlineKeyboardButton fifthButton = new InlineKeyboardButton("Рекомендации по обустройству дома" +
                " для взрослой собаки");
        fifthButton.callbackData("Кнопка 2.5");
        InlineKeyboardButton sixthButton = new InlineKeyboardButton("Рекомендации по обустройству дома" +
                " для собаки с ограниченными возможностями");
        sixthButton.callbackData("Кнопка 2.6");
        InlineKeyboardButton seventhButton = new InlineKeyboardButton("Советы кинолога по первичному " +
                "общению с собакой");
        seventhButton.callbackData("Кнопка 2.7");
        InlineKeyboardButton eightButton = new InlineKeyboardButton("Рекомендации по проверенным кинологам " +
                "для дальнейшего обращения к ним");
        eightButton.callbackData("Кнопка 2.8");
        InlineKeyboardButton ninthButton = new InlineKeyboardButton("Причины согласно которым могут " +
                "отказать забрать собаку из приюта");
        ninthButton.callbackData("Кнопка 2.9");
        InlineKeyboardButton tenthButton = new InlineKeyboardButton("Записать " +
                "контактные данные для связи");
        tenthButton.callbackData("Кнопка 2.10");
        InlineKeyboardButton eleventhButton = new InlineKeyboardButton("Позвать волонтера");
        eleventhButton.callbackData("Кнопка 2.11");
        InlineKeyboardButton twelfthButton = new InlineKeyboardButton("Вернуться на главное меню");
        twelfthButton.callbackData("Кнопка 2.12");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(firstButton, secondButton);
        inlineKeyboardMarkup.addRow(thirdButton, fourthButton);
        inlineKeyboardMarkup.addRow(fifthButton, sixthButton);
        inlineKeyboardMarkup.addRow(seventhButton, eightButton);
        inlineKeyboardMarkup.addRow(ninthButton, tenthButton);
        inlineKeyboardMarkup.addRow(eleventhButton, twelfthButton);
        SendMessage sendMessage = new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(sendMessage);
    }

    public void showReportMenu(Long chatId) {
        String text = " Вы зашли в раздел предоставления отчетов. ";
        InlineKeyboardButton firstButton = new InlineKeyboardButton("Прислать форму ежедневного отчета");
        firstButton.callbackData("Кнопка 3.1");
        InlineKeyboardButton secondButton = new InlineKeyboardButton("Отправить отчет");
        secondButton.callbackData("Кнопка 3.2");
        InlineKeyboardButton thirdButton = new InlineKeyboardButton("Вернуться на главное меню");
        thirdButton.callbackData("Кнопка 3.3");
        InlineKeyboardButton fourthButton = new InlineKeyboardButton("Позвать волонтера");
        fourthButton.callbackData("Кнопка 3.4");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(firstButton, secondButton);
        inlineKeyboardMarkup.addRow(thirdButton, fourthButton);
        SendMessage sendMessage = new SendMessage(chatId, text).replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(sendMessage);
    }
}
