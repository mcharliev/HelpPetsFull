package pro.sky.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.springframework.util.StringUtils;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.service.OwnerService;
import pro.sky.telegrambot.service.ReportService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class ImageHandler implements Handler {
    private final TelegramBot telegramBot;
    private final OwnerService ownerService;
    private final ReportService reportService;

    public ImageHandler(TelegramBot telegramBot,
                        OwnerService ownerService,
                        ReportService reportService) {
        this.telegramBot = telegramBot;
        this.ownerService = ownerService;
        this.reportService = reportService;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        Message message = update.message();
        Owner owner = ownerService.findOwnerByChatId(chatId);
        /* если пользователь присылает фото в чат и он не зарегестрирован в бд как owner(owner=null)
         * информирую его, чтобы необходимо сначала зарегестрироваться и выхожу из метода с помощью return*/
        if (owner == null) {
            sendMessage(chatId, "Вы не зарегестрированы, пожалуйста обратитесь к волонтеру," +
                    " чтобы он вас зарегестрировал");
            return;
        }
        PhotoSize photoSize = message.photo()[message.photo().length - 1];
        GetFileResponse getFileResponse = telegramBot.execute(new GetFile(photoSize.fileId()));
        Report report = reportService.findLastReportByOwnerId(owner.getId());
        LocalDateTime dateTimeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        try {
            /* если report = null значит записей в бд нету, я создаю новый отчет и сохраняю его в бд
             * и информирую овнера, что отчет успешно загрузился, далее выхожу из метода с помощью
             * return, так как в 70 строке геттер report.getStringReport() выдает NullPointerException
             * не смотря на то, что данный геттер возвращает Optional */
            if (report == null) {
                reportService.saveImageInNewReport(telegramBot.getFileContent(getFileResponse.file()),
                        owner,
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                sendMessage(chatId, "Вы успешно загрузили фото отчет, " +
                        " пожалуйста не забудьте загрузить текстовый отчет отчет");
                return;
            }
            /* так как по условию овнеры должны присылать отчеты раз в день, значит отчеты должны быть разграничены по дням,
             * если настоящая дата isBefore последней даты отчета к который прибавлен один день, фото
             * сохраняется  в существующий отчет */
            if (dateTimeNow.isBefore(report.getDateOfLastReport().plusDays(1))) {
                reportService.saveImageInExistingReport(report,
                        telegramBot.getFileContent(getFileResponse.file()),
                        owner,
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            }
            /* если настоящая дата isAfter последней даты отчета к который прибавлен один день,
             *создается новый отчет и фото сохраняется туда
             */
            else if (dateTimeNow.isAfter(report.getDateOfLastReport().plusDays(1))) {
                reportService.saveImageInNewReport(telegramBot.getFileContent(getFileResponse.file()),
                        owner,
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            }

            sendInfoIfOnlyImageReportLoaded(report.getStringReport(), chatId);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
