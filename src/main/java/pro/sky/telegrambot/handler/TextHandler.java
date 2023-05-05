package pro.sky.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.keyboard.InlineKeyboard;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.CatShelterUsersRepository;
import pro.sky.telegrambot.repository.DogShelterUsersRepository;
import pro.sky.telegrambot.repository.UserContextRepository;
import pro.sky.telegrambot.service.CatOwnerReportService;
import pro.sky.telegrambot.service.CatOwnerService;
import pro.sky.telegrambot.service.DogOwnerService;
import pro.sky.telegrambot.service.DogOwnerReportService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHandler implements Handler {
    private final TelegramBot telegramBot;
    private final DogShelterUsersRepository dogShelterUsersRepository;
    private final CatShelterUsersRepository catShelterUsersRepository;
    private final DogOwnerService dogOwnerService;
    private final CatOwnerService catOwnerService;
    private final DogOwnerReportService dogOwnerReportService;
    private final CatOwnerReportService catOwnerReportService;
    private final UserContextRepository userContextRepository;

    private final Pattern pattern = Pattern.compile("\\d{11} [А-я]+");

    public TextHandler(TelegramBot telegramBot,
                       DogShelterUsersRepository dogShelterUsersRepository,
                       CatShelterUsersRepository catShelterUsersRepository,
                       DogOwnerService dogOwnerService,
                       CatOwnerService catOwnerService,
                       DogOwnerReportService dogOwnerReportService,
                       CatOwnerReportService catOwnerReportService,
                       UserContextRepository userContextRepository) {
        this.telegramBot = telegramBot;
        this.dogShelterUsersRepository = dogShelterUsersRepository;
        this.catShelterUsersRepository = catShelterUsersRepository;
        this.dogOwnerService = dogOwnerService;
        this.catOwnerService = catOwnerService;
        this.dogOwnerReportService = dogOwnerReportService;
        this.catOwnerReportService = catOwnerReportService;
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
        } else if (matcher.find()) {
            String result = matcher.group(0);
            findMatchesAndSaveInBd(result, chatId);
        } else if (text.length() > 30) {
            saveDogOwnerTextReport(chatId, text);
            saveCatOwnerTextReport(chatId, text);
        }
    }

    private void findMatchesAndSaveInBd(String foundString, Long chatId) {
        foundString = foundString.replaceAll(" ", "");
        String phoneNumber = foundString.substring(0, 10);
        String name = foundString.substring(11);
        boolean dogShelter = userContextRepository.findByChatId(chatId)
                .map(UserContext::isDogShelter)
                .orElse(false);
        boolean catShelter = userContextRepository.findByChatId(chatId)
                .map(UserContext::isCatShelter)
                .orElse(false);
        if (dogShelter) {
            DogShelterUser dogShelterUser = new DogShelterUser();
            dogShelterUser.setPhoneNumber(phoneNumber);
            dogShelterUser.setName(name);
            dogShelterUsersRepository.save(dogShelterUser);
        } else if (catShelter) {
            CatShelterUser catShelterUser = new CatShelterUser();
            catShelterUser.setPhoneNumber(phoneNumber);
            catShelterUser.setName(name);
            catShelterUsersRepository.save(catShelterUser);
        }
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

    private void saveDogOwnerTextReport(Long chatId, String textReport) {
        LocalDateTime dateTimeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Optional<DogOwner> optDogOwner = dogOwnerService.findDogOwnerByChatId(chatId);
        if (optDogOwner.isPresent()) {
            DogOwner dogOwner = optDogOwner.get();
            Optional<DogOwnerReport> optDogOwnerReport =
                    dogOwnerReportService.findLastReportByOwnerId(dogOwner.getId());
            if (optDogOwnerReport.isPresent()) {
                DogOwnerReport dogOwnerReport = optDogOwnerReport.get();
                if (dateTimeNow.isBefore(dogOwnerReport.getDateOfLastReport().plusDays(1))) {
                    dogOwnerReportService.saveTextInExistingReport(dogOwnerReport,
                            textReport,
                            dogOwner,
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                } else if (dateTimeNow.isAfter(dogOwnerReport.getDateOfLastReport().plusDays(1))) {
                    dogOwnerReportService.saveTextInNewReport(textReport,
                            dogOwner,
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                }
                sendInfoIfOnlyStringReportLoaded(optDogOwnerReport.get().getPhotoReport(), chatId);
            } else {
                dogOwnerReportService.saveTextInNewReport(textReport,
                        dogOwner,
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                sendMessage(chatId, "Вы успешно загрузили текстовый отчет, " +
                        " пожалуйста не забудьте загрузить фото отчет ");
            }
        }
    }

    private void saveCatOwnerTextReport(Long chatId, String textReport) {
        LocalDateTime dateTimeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Optional<CatOwner> optCatOwner = catOwnerService.findCatOwnerByChatId(chatId);
        if (optCatOwner.isPresent()) {
            CatOwner catOwner = optCatOwner.get();
            Optional<CatOwnerReport> optCatOwnerReport =
                    catOwnerReportService.findLastReportByOwnerId(catOwner.getId());
            if (optCatOwnerReport.isPresent()) {
                CatOwnerReport catOwnerReport = optCatOwnerReport.get();
                if (dateTimeNow.isBefore(catOwnerReport.getDateOfLastReport().plusDays(1))) {
                    catOwnerReportService.saveTextInExistingReport(catOwnerReport,
                            textReport,
                            catOwner,
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                } else if (dateTimeNow.isAfter(catOwnerReport.getDateOfLastReport().plusDays(1))) {
                    catOwnerReportService.saveTextInNewReport(textReport,
                            catOwner,
                            LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                }
                sendInfoIfOnlyStringReportLoaded(optCatOwnerReport.get().getPhotoReport(), chatId);
            } else {
                catOwnerReportService.saveTextInNewReport(textReport,
                        catOwner,
                        LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                sendMessage(chatId, "Вы успешно загрузили текстовый отчет, " +
                        " пожалуйста не забудьте загрузить фото отчет ");
            }
        }
    }
}
