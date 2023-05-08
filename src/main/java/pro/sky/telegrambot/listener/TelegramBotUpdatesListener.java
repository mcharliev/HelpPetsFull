package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.handler.CallBackQueryHandler;
import pro.sky.telegrambot.handler.Handler;
import pro.sky.telegrambot.handler.ImageHandler;
import pro.sky.telegrambot.handler.TextHandler;
import pro.sky.telegrambot.repository.UserContextRepository;
import pro.sky.telegrambot.service.*;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final DogOwnerService dogOwnerService;
    private final CatOwnerService catOwnerService;
    private final DogOwnerReportService dogOwnerReportService;
    private final CatOwnerReportService catOwnerReportService;
    private final TelegramBot telegramBot;
    private final UserContextRepository userContextRepository;
    private final DogShelterUserService dogShelterUserService;
    private final CatShelterUserService catShelterUserService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public TelegramBotUpdatesListener(DogOwnerService dogOwnerService,
                                      CatOwnerService catOwnerService,
                                      DogOwnerReportService dogOwnerReportService,
                                      CatOwnerReportService catOwnerReportService,
                                      TelegramBot telegramBot,
                                      UserContextRepository userContextRepository,
                                      DogShelterUserService dogShelterUserService,
                                      CatShelterUserService catShelterUserService) {
        this.dogOwnerService = dogOwnerService;
        this.catOwnerService = catOwnerService;
        this.dogOwnerReportService = dogOwnerReportService;
        this.catOwnerReportService = catOwnerReportService;
        this.telegramBot = telegramBot;
        this.userContextRepository = userContextRepository;
        this.dogShelterUserService = dogShelterUserService;
        this.catShelterUserService = catShelterUserService;
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
                    Handler callBackHandler = new CallBackQueryHandler(telegramBot,
                            userContextRepository);
                    callBackHandler.handle(update);
                    return;
                }
                if (update.message().text() != null) {
                    Handler textHandler = new TextHandler(telegramBot,
                            dogShelterUserService,
                            catShelterUserService,
                            dogOwnerService,
                            catOwnerService,
                            dogOwnerReportService,
                            catOwnerReportService,
                            userContextRepository);
                    textHandler.handle(update);
                }
                if (update.message().photo() != null) {
                    Handler imageHandler = new ImageHandler(telegramBot,
                            dogOwnerService,
                            catOwnerService,
                            dogOwnerReportService,
                            catOwnerReportService);
                    imageHandler.handle(update);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}







