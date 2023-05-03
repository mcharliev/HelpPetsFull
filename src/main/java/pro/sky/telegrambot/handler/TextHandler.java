package pro.sky.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.keyboard.InlineKeyboard;
import pro.sky.telegrambot.model.ContactDetails;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.UserContext;
import pro.sky.telegrambot.repository.UserContextRepository;
import pro.sky.telegrambot.service.ContactDetailsService;
import pro.sky.telegrambot.service.OwnerService;
import pro.sky.telegrambot.service.ReportService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHandler implements Handler {
    private final TelegramBot telegramBot;
    private final ContactDetailsService contactDetailsService;
    private final OwnerService ownerService;
    private final ReportService reportService;
    private final UserContextRepository userContextRepository;

    private final Pattern pattern = Pattern.compile("\\d{11} [А-я]+");

    public TextHandler(TelegramBot telegramBot,
                       ContactDetailsService contactDetailsService,
                       OwnerService ownerService,
                       ReportService reportService, UserContextRepository userContextRepository) {
        this.telegramBot = telegramBot;
        this.contactDetailsService = contactDetailsService;
        this.ownerService = ownerService;
        this.reportService = reportService;
        this.userContextRepository = userContextRepository;
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Long chatId = message.chat().id();
        String text = message.text();
        Matcher matcher = pattern.matcher(text);
        InlineKeyboard inlineKeyboard = new InlineKeyboard(telegramBot);

        /*если пользователь отправил команду /start вызывается стартовое меню */
        if ("/start".equals(text)) {
            boolean catShelter = userContextRepository.findByChatId(chatId)
                    .map(UserContext::isCatShelter)
                    .orElse(false);
            boolean dogShelter = userContextRepository.findByChatId(chatId)
                    .map(UserContext::isDogShelter)
                    .orElse(false);
            if (dogShelter) {
                inlineKeyboard.showDogShelterMenu(chatId);
            } else if (catShelter) {
                inlineKeyboard.showCatShelterMenu(chatId);
            } else {
                inlineKeyboard.chooseShelterMenu(chatId);
            }
        }
        /*если входной текст от пользователя совпал с паттерном вызывается метод findMatchesAndSaveInBd
         который достается из сообщения имя и телефон и сохраняет эту контактную информацию в бд в таблицу
         details_service*/
        else if (matcher.find()) {
            String result = matcher.group(0);
            findMatchesAndSaveInBd(result, chatId);
        }
        /*если длина сообщения больше 30 символов значит это сообщение является текстовым отчетом*/
        else if (text.length() > 30) {
            Owner owner = ownerService.findOwnerByChatId(chatId);
            /* если пользователь присылает текстовый отчет в чат и он не зарегестрирован в бд как owner(owner=null)
             * информирую его, чтобы необходимо сначала зарегестрироваться и выхожу из метода с помощью return*/
            if (owner == null) {
                sendMessage(chatId, "Вы не зарегестрированы, пожалуйста обратитесь к волонтеру," +
                        " чтобы он вас зарегестрировал");
                return;
            }
            Report report = reportService.findLastReportByOwnerId(owner.getId());
            LocalDateTime dateTimeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            /* если report = null значит записей в бд нету, я создаю новый отчет и сохраняю его в бд
             * и информирую овнера, что отчет успешно загрузился, далее выхожу из метода с помощью
             * return, так как в 89 строке геттер report.getPhotoReport() выдает NullPointerException
             * не смотря на то, что данный геттер возвращает Optional */
            if (report == null) {
                reportService.saveTextInNewReport(text,
                        owner,
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                sendMessage(chatId, "Вы успешно загрузили текстовый отчет, " +
                        " пожалуйста не забудьте загрузить фото отчет");
                return;
            }
            /* так как по условию овнеры должны присылать отчеты раз в день, значит отчеты должны быть разграничены по дням,
             * если настоящая дата isBefore последней даты отчета к который прибавлен один день, текст
             * сохраняется  в существующий отчет */
            if (dateTimeNow.isBefore(report.getDateOfLastReport().plusDays(1))) {
                reportService.saveTextInExistingReport(report,
                        text,
                        owner,
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            }
            /* если настоящая дата isAfter последней даты отчета к который прибавлен один день,
             *создается новый отчет и текст сохраняется туда*/
            else if (dateTimeNow.isAfter(report.getDateOfLastReport().plusDays(1))) {
                reportService.saveTextInNewReport(text,
                        owner,
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            }
            sendInfoIfOnlyStringReportLoaded(report.getPhotoReport(), chatId);
        } else {
            sendMessage(chatId, " Ваш текстовый отчет недостаточно подробный, пожалуйста заполните отчет" +
                    "подробнее");
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

    /* после загрузки текста в отчет, метод проверяет лежит ли в Optional фото отчет,
     * если лежит информирует овнера, что текст загружен, если нет информирует, что текст загружен
     * и просит не забыть загрузить фото отчет*/
    private void sendInfoIfOnlyStringReportLoaded(Optional<byte[]> image,
                                                  Long chatId) {
        if (image.isPresent()) {
            sendMessage(chatId, "Вы успешно загрузили текстовый отчет");
        } else {
            sendMessage(chatId, "Вы успешно загрузили текстовый отчет, " +
                    " пожалуйста не забудьте загрузить фото отчет");
        }
    }
}
