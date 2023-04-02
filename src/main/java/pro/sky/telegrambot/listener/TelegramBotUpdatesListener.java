package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.handlers.CallBackQueryHandler;
import pro.sky.telegrambot.handlers.Handler;
import pro.sky.telegrambot.keyboard.InlineKeyboard;
import pro.sky.telegrambot.model.ContactDetails;
import pro.sky.telegrambot.service.ContactDetailsService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final ContactDetailsService contactDetailsService;
    private  Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final Pattern pattern = Pattern.compile("\\d{11} [А-я]+");


    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      ContactDetailsService contactDetailsService) {
        this.telegramBot = telegramBot;
        this.contactDetailsService = contactDetailsService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);
                if (update.callbackQuery() != null) {
                    Handler callBackHandler = new CallBackQueryHandler(telegramBot);
                    callBackHandler.handle(update);
                    return;
                }
                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();

                if ("/start".equals(text)) {
                    InlineKeyboard inlineKeyboard = new InlineKeyboard(telegramBot);
                    inlineKeyboard.showStartMenu(chatId);
                } else if (text != null) {
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        String result = matcher.group(0);
                        findMatchesAndSaveInBd(result, chatId);
                    } else {
                        sendMessage(chatId, "Некорректный формат сообщения");
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        telegramBot.execute(sendMessage);
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
}
