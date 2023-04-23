package pro.sky.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.keyboard.InlineKeyboard;
import pro.sky.telegrambot.model.ContactDetails;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.service.ContactDetailsService;
import pro.sky.telegrambot.service.OwnerService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHandler implements Handler {
    private final TelegramBot telegramBot;
    private final ContactDetailsService contactDetailsService;
    private final OwnerService ownerService;

    private final Pattern pattern = Pattern.compile("\\d{11} [А-я]+");

    public TextHandler(TelegramBot telegramBot,
                       ContactDetailsService contactDetailsService,
                       OwnerService ownerService) {
        this.telegramBot = telegramBot;
        this.contactDetailsService = contactDetailsService;
        this.ownerService = ownerService;
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Long chatId = message.chat().id();
        String text = message.text();
        String name = message.chat().firstName();
        Matcher matcher = pattern.matcher(text);
        LocalDateTime dateOfStartProbation = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);


        if ("/start".equals(text)) {
            InlineKeyboard inlineKeyboard = new InlineKeyboard(telegramBot);
            inlineKeyboard.showStartMenu(chatId);
        } else if (matcher.find()) {
            String result = matcher.group(0);
            findMatchesAndSaveInBd(result, chatId);
        } else {
            Owner owner = ownerService.findOwnerByChatId(chatId);
            if (owner != null && text.length() > 30) {
//                owner.setStringReport(text);
//                owner.setDateOfLastReport(dateOfStartProbation);
                ownerService.saveOwner(owner);
//                if (owner.getPhotoReport() == null) {
//                    sendMessage(chatId, "Вы успешно загрузили текстовый отчет ," +
//                            " пожалуйста не забудьте предоставить фото отчет");
//                }
            } else {
                sendMessage(chatId, "Отчет недостаточно подробный, пожалуйста заполните отчет подробнее");
            }
        }
    }

    private void findMatchesAndSaveInBd(String foundString, Long chatId) {
        foundString = foundString.replaceAll(" ", "");
        String phoneNumber = foundString.substring(0, 10);
        String name = foundString.substring(11);
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setChatId(chatId);
        contactDetails.setPhoneNumber(phoneNumber);
        contactDetails.setName(name);
        contactDetailsService.save(contactDetails);
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        telegramBot.execute(sendMessage);
    }
}
