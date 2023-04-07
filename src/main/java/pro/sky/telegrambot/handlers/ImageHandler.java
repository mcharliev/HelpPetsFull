package pro.sky.telegrambot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.service.OwnerService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ImageHandler implements Handler {
    private final TelegramBot telegramBot;
    private final OwnerService ownerService;

    public ImageHandler(TelegramBot telegramBot,
                        OwnerService ownerService) {
        this.telegramBot = telegramBot;
        this.ownerService = ownerService;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        Owner owner = ownerService.findOwnerByChatId(chatId);
        Message message = update.message();
        PhotoSize photoSize = message.photo()[message.photo().length - 1];
        LocalDateTime dateOfLastRepost = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        GetFileResponse getFileResponse = telegramBot.execute(
                new GetFile(photoSize.fileId()));
        try {
            if (owner.getPhotoReport() != null) {
                owner.setPhotoReport(null);
                owner.setPhotoReport(telegramBot.getFileContent(getFileResponse.file()));
                owner.setDateOfLastReport(dateOfLastRepost);
            } else {
                owner.setPhotoReport(telegramBot.getFileContent(getFileResponse.file()));
                owner.setDateOfLastReport(dateOfLastRepost);
                informAboutUploadingPhoto(chatId);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ownerService.saveOwner(owner);
    }

    public void informAboutUploadingPhoto(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Вы успешно загрузили фото отчет," +
                " пожалуйста не забудьте предоставить текстовый отчет в который входит: " +
                " Рацион животного; общее самочувствие и привыкание к новому месту;" +
                " Изменение в поведении: отказ от старых привычек, приобретение новых. ");
        telegramBot.execute(sendMessage);
    }
}
