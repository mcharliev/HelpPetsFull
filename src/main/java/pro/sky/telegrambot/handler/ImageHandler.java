package pro.sky.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import pro.sky.telegrambot.model.CatOwner;
import pro.sky.telegrambot.model.CatOwnerReport;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.model.DogOwnerReport;
import pro.sky.telegrambot.service.CatOwnerReportService;
import pro.sky.telegrambot.service.CatOwnerService;
import pro.sky.telegrambot.service.DogOwnerService;
import pro.sky.telegrambot.service.DogOwnerReportService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class ImageHandler implements Handler {
    private final TelegramBot telegramBot;
    private final DogOwnerService dogOwnerService;
    private final CatOwnerService catOwnerService;
    private final DogOwnerReportService dogOwnerReportService;
    private final CatOwnerReportService catOwnerReportService;

    public ImageHandler(TelegramBot telegramBot,
                        DogOwnerService ownerService,
                        CatOwnerService catOwnerService,
                        DogOwnerReportService dogOwnerReportService,
                        CatOwnerReportService catOwnerReportService) {
        this.telegramBot = telegramBot;
        this.dogOwnerService = ownerService;
        this.catOwnerService = catOwnerService;
        this.dogOwnerReportService = dogOwnerReportService;
        this.catOwnerReportService = catOwnerReportService;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        Message message = update.message();
        LocalDateTime dateTimeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        PhotoSize photoSize = message.photo()[message.photo().length - 1];
        GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));

        Optional<DogOwner> optDogOwner = dogOwnerService.findDogOwnerByChatId(chatId);
        if (optDogOwner.isPresent()) {
            DogOwner dogOwner = optDogOwner.get();
            try {
                Optional<DogOwnerReport> optDogOwnerReport =
                        dogOwnerReportService.findLastReportByOwnerId(dogOwner.getId());
                if (optDogOwnerReport.isPresent()) {
                    DogOwnerReport dogOwnerReport = optDogOwnerReport.get();
                    if (dateTimeNow.isBefore(dogOwnerReport.getDateOfLastReport().plusDays(1))) {
                        dogOwnerReportService.saveImageInExistingReport(dogOwnerReport,
                                telegramBot.getFileContent(getFileResponse.file()),
                                dogOwner,
                                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                    } else if (dateTimeNow.isAfter(dogOwnerReport.getDateOfLastReport().plusDays(1))) {
                        dogOwnerReportService.saveImageInNewReport(telegramBot.getFileContent(getFileResponse.file()),
                                dogOwner,
                                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                    }
                    sendInfoIfOnlyImageReportLoaded(optDogOwnerReport.get().getStringReport(),chatId);
                } else {
                    dogOwnerReportService.saveImageInNewReport(telegramBot.getFileContent(getFileResponse.file()),
                            dogOwner,
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                    sendMessage(chatId, "Вы успешно загрузили фото отчет, " +
                            " пожалуйста не забудьте загрузить текстовый отчет отчет");
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        Optional<CatOwner> optCatOwner = catOwnerService.findCatOwnerByChatId(chatId);
        if (optCatOwner.isPresent()) {
            CatOwner catOwner = optCatOwner.get();
            try {
                Optional<CatOwnerReport> optCatOwnerReport =
                        catOwnerReportService.findLastReportByOwnerId(catOwner.getId());
                if (optCatOwnerReport.isPresent()) {
                    CatOwnerReport catOwnerReport = optCatOwnerReport.get();
                    if (dateTimeNow.isBefore(catOwnerReport.getDateOfLastReport().plusDays(1))) {
                        catOwnerReportService.saveImageInExistingReport(catOwnerReport,
                                telegramBot.getFileContent(getFileResponse.file()),
                                catOwner,
                                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                    } else if (dateTimeNow.isAfter(catOwnerReport.getDateOfLastReport().plusDays(1))) {
                        catOwnerReportService.saveImageInNewReport(telegramBot.getFileContent(getFileResponse.file()),
                                catOwner,
                                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                    }
                    sendInfoIfOnlyImageReportLoaded(optCatOwnerReport.get().getStringReport(),chatId);
                } else {
                    catOwnerReportService.saveImageInNewReport(telegramBot.getFileContent(getFileResponse.file()),
                            catOwner,
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                    sendMessage(chatId, "Вы успешно загрузили фото отчет, " +
                            " пожалуйста не забудьте загрузить текстовый отчет отчет");
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        telegramBot.execute(sendMessage);
    }

    /* после загрузки фото в отчет, метод проверяет лежит ли в Optional текстовый отчет,
     * если лежит информирует овнера, что фото загружено, если нет информирует, что фото загружено
     * и просит не забыть загрузить текстовый отчет*/
    private void sendInfoIfOnlyImageReportLoaded(Optional<String> string,
                                                 Long chatId) {
        if (string.isPresent()) {
            sendMessage(chatId, "Вы успешно загрузили фото отчет");
        } else {
            sendMessage(chatId, "Вы успешно загрузили фото отчет, " +
                    " пожалуйста не забудьте загрузить текстовый отчет отчет");
        }
    }
}
