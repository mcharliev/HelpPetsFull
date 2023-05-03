package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.enam.ProbationaryStatus;
import pro.sky.telegrambot.handler.CallBackQueryHandler;
import pro.sky.telegrambot.handler.Handler;
import pro.sky.telegrambot.handler.ImageHandler;
import pro.sky.telegrambot.handler.TextHandler;
import pro.sky.telegrambot.model.Owner;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.repository.CatShelterUsersRepository;
import pro.sky.telegrambot.repository.DogShelterUsersRepository;
import pro.sky.telegrambot.repository.UserContextRepository;
import pro.sky.telegrambot.service.OwnerService;
import pro.sky.telegrambot.service.ReportService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final OwnerService ownerService;
    private final ReportService reportService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final UserContextRepository userContextRepository;
    private final DogShelterUsersRepository dogShelterUsersRepository;
    private final CatShelterUsersRepository catShelterUsersRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      OwnerService ownerService,
                                      ReportService reportService,
                                      UserContextRepository userContextRepository,
                                      DogShelterUsersRepository dogShelterUsersRepository,
                                      CatShelterUsersRepository catShelterUsersRepository
    ) {
        this.telegramBot = telegramBot;
        this.ownerService = ownerService;
        this.reportService = reportService;
        this.userContextRepository = userContextRepository;
        this.dogShelterUsersRepository = dogShelterUsersRepository;
        this.catShelterUsersRepository = catShelterUsersRepository;
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
                            catShelterUsersRepository,
                            dogShelterUsersRepository,
                            ownerService,
                            reportService,
                            userContextRepository);
                    textHandler.handle(update);
                }
                if (update.message().photo() != null) {
                    Handler imageHandler = new ImageHandler(telegramBot, ownerService, reportService);
                    imageHandler.handle(update);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(fixedDelay = 59_000L)
    public void informOwner() {
        List<Owner> owners = ownerService.findAllOwners();
        List<Report> reports = reportService.findAllReports();
        informOwnerWhenHePassed(owners);
        informOwnerWhenHeNotPassed(owners);
        informOwnerWhenHeBadReporting(owners);
        checkDeadline(reports);
        informOwnerWhenDeadlineExtended(owners);
    }


    /* Если овнер прошел исп срок, волонтер меняет статус на PASSED, метод проверяет всех овнеров на
     * данный статус если овнеры найдены, бот информирует их о прохождении исп срока, далее метод меняет статус
     * у всех овнеровна FINALLY_PASSED чтобы метод больше не информировал овнеров о прохождении исп срока.*/
    private void informOwnerWhenHePassed(List<Owner> owners) {
        owners.stream().filter(element -> element.getProbationaryStatus().equals(ProbationaryStatus.PASSED))
                .peek(element -> telegramBot.execute(
                        new SendMessage(element.getChatId(), "Добрый день, поздравляем" +
                                " ваш испытательный срок окончен")))
                .filter(element -> element.getProbationaryStatus().equals(ProbationaryStatus.PASSED))
                .peek(element -> element.setProbationaryStatus(ProbationaryStatus.FINALLY_PASSED))
                .forEach(ownerService::saveOwner);
    }


    /*  Если овнер прошел исп срок, волонтер меняет статус на NOT_PASSED, метод проверяет всех овнеров на данный статус
     * если овнеры найдены, бот информирует их о прохождении исп срока, далее метод меняет статус у всех овнеров
     * на FINALLY_PASSED чтобы метод больше не информировал овнеров о прохождении исп срока.*/

    private void informOwnerWhenHeNotPassed(List<Owner> owners) {
        owners.stream().filter(element -> element.getProbationaryStatus().equals(ProbationaryStatus.NOT_PASSED))
                .peek(element -> telegramBot.execute(
                        new SendMessage(element.getChatId(), "Добрый день" +
                                " к сожалению вы не прошли испытательный срок, пожалуйста верните" +
                                " животное в приют.")))
                .filter(element -> element.getProbationaryStatus().equals(ProbationaryStatus.NOT_PASSED))
                .peek(element -> element.setProbationaryStatus(ProbationaryStatus.FINALLY_NOT_PASSED))
                .forEach(ownerService::saveOwner);
    }


    /* Если просмотрев отчеты овнеров волонтер решил, что овнер предоставляет отчеты плохо, волонтер меняет
     * статус овнера на BAD_REPORTING, далее метод находит овнеров с таким статусом бот информирует овнера, что он
     * предоставляет отчеты плохо и просит исправиться, далее метод меняет статус у всех овнеров
     * на UNSATISFACTORY чтобы метод больше не информировал овнеров о прохождении исп срока
     * */
    private void informOwnerWhenHeBadReporting(List<Owner> owners) {
        owners.stream().filter(element -> element.getProbationaryStatus().equals(ProbationaryStatus.BAD_REPORTING))
                .peek(element -> telegramBot.execute(
                        new SendMessage(element.getChatId(), "Дорогой усыновитель, мы заметили, что вы заполняете" +
                                " отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию." +
                                " В противном случае волонтеры приюта будут обязаны самолично проверять условия" +
                                " содержания собаки")))
                .filter(element -> element.getProbationaryStatus().equals(ProbationaryStatus.BAD_REPORTING))
                .peek(element -> element.setProbationaryStatus(ProbationaryStatus.UNSATISFACTORY))
                .forEach(ownerService::saveOwner);
    }


    /* Если волантер решил продлить исп срок овнера, он должен вручную добавить срок продления в бд и
     * поменять статус усыновителя на EXTENDED. Далее метод проверяет у овнеров срок продления и статус,
     * если срок продления больше 0 и статус равен EXTENDED бот информирует овнера о продлении исп срока. Далее метод
     * перезаписывает статус овнера на FINALLY_EXTENDED, чтобы бот повторно не информировал овнера
     * */
    private void informOwnerWhenDeadlineExtended(List<Owner> owners) {
        owners.stream().filter(element -> element.getPeriodExtend() > 0
                        && element.getProbationaryStatus().equals(ProbationaryStatus.EXTENDED))
                .peek(element -> telegramBot.execute(
                        new SendMessage(element.getChatId(), "Дорогой усыновитель, ваш испытаельный срок продлен на " +
                                element.getPeriodExtend() + " дней")))
                .filter(element -> element.getPeriodExtend() > 0
                        && element.getProbationaryStatus().equals(ProbationaryStatus.EXTENDED))
                .peek(element -> element.setProbationaryStatus(ProbationaryStatus.FINALLY_EXTENDED))
                .forEach(ownerService::saveOwner);
    }


    /* дату последнего отчета я увеличиваю на один день, если увеличенная дата будет равна настоящей дате
     * бот проинформирует пользователя, что он плохо предоставляет отчеты. Далее дату последнего отчета я увеличиваю
     * на два дня, если увеличенная дата будет равна настоящей дате бот свяжется с волантером и предоставит ему
     * данные на пользователя который плохо заполняет отчеты*/
    private void checkDeadline(List<Report> reports) {
        LocalDateTime localDateTimeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        reports.forEach(element -> {
            if (element.getDateOfLastReport().plusDays(1).equals(localDateTimeNow)) {
                informOwner(element.getOwner());
            } else if (element.getDateOfLastReport().plusDays(2).equals(localDateTimeNow)) {
                informVolunteer(element.getOwner());
            }
        });
    }


    /*    если с даты последнего отчета прошел один день, этот метод информирует овнера, чтобы он
     * заполнял отчеты лучше
     */
    private void informOwner(Owner owner) {
        telegramBot.execute(new SendMessage(owner.getChatId(), "Дорогой усыновитель, мы заметили," +
                " что за последние сутки вы предоставляли не подробные отчеты о животном, пожалуйста" +
                " отнеситесь серьезно к предоставлению отчетов"));
    }

    /* если с даты последнего отчета прошло два дня, этот метод информирует волонтера,
     * что овнер плохо заполняет отчеты
     */
    private void informVolunteer(Owner owner) {
        Long VOLUNTEER_CHAT_ID = 512213990L;
        telegramBot.execute(new SendMessage(VOLUNTEER_CHAT_ID, "Пользователь," +
                " по имени: " + owner.getName() + " id: " + owner.getChatId() + " более двух суток не" +
                " заполнял отчет, пожалуйста свяжитесь с ним"));
    }
}







