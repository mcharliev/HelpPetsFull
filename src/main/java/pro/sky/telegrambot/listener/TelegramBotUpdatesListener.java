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
import pro.sky.telegrambot.service.ContactDetailsService;
import pro.sky.telegrambot.service.OwnerService;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final ContactDetailsService contactDetailsService;
    private final OwnerService ownerService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      ContactDetailsService contactDetailsService,
                                      OwnerService ownerService) {
        this.telegramBot = telegramBot;
        this.contactDetailsService = contactDetailsService;
        this.ownerService = ownerService;
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
                if (update.message().text() != null) {
                    Handler textHandler = new TextHandler(telegramBot, contactDetailsService, ownerService);
                    textHandler.handle(update);
                }
                if (update.message().photo() != null) {
                    Handler imageHandler = new ImageHandler(telegramBot, ownerService);
                    imageHandler.handle(update);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    // @Scheduled(cron = "0 0/1 * * * *") //на первой секунде каждой минуты
    @Scheduled(fixedDelay = 59_000L)
    public void informOwner() {

        List<Owner> owners = ownerService.findAllOwners();

        informOwnerWhenHePassed(owners);

        informOwnerWhenHeNotPassed(owners);

        informOwnerWhenHeBadReporting(owners);
//
//        showInfoWhenFailedDeadline(owners);

        informOwnerWhenDeadlineExtended(owners);
    }

    /**
     * Если овнер прошел исп срок, волонтер меняет статус на PASSED, метод проверяет всех овнеров на
     * данный статус если овнеры найдены, бот информирует их о прохождении исп срока, далее метод меняет статус
     * у всех овнеровна FINALLY_PASSED чтобы метод больше не информировал овнеров о прохождении исп срока.
     */
    private void informOwnerWhenHePassed(List<Owner> owners) {
        owners.stream().filter(element -> element.getProbationaryStatus().equals(ProbationaryStatus.PASSED))
                .peek(element -> telegramBot.execute(
                        new SendMessage(element.getChatId(), "Добрый день, поздравляем" +
                                " ваш испытательный срок окончен")))
                .filter(element -> element.getProbationaryStatus().equals(ProbationaryStatus.PASSED))
                .peek(element -> element.setProbationaryStatus(ProbationaryStatus.FINALLY_PASSED))
                .forEach(ownerService::saveOwner);
    }

    /**
     * Если овнер прошел исп срок, волонтер меняет статус на NOT_PASSED, метод проверяет всех овнеров на данный статус
     * если овнеры найдены, бот информирует их о прохождении исп срока, далее метод меняет статус у всех овнеров
     * на FINALLY_PASSED чтобы метод больше не информировал овнеров о прохождении исп срока.
     */
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

    /**
     * Если просмотрев отчеты овнеров волонтер решил, что овнер предоставляет отчеты плохо, волонтер меняет
     * статус овнера на BAD_REPORTING, далее метод находит овнеров с таким статусом бот информирует овнера, что он
     * предоставляет отчеты плохо и просит исправиться, далее метод меняет статус у всех овнеров
     * на UNSATISFACTORY чтобы метод больше не информировал овнеров о прохождении исп срока
     */
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

    /**
     * дату последнего отчета я увеличиваю на один день, если увеличенная дата будет равна настоящей дате
     * бот проинформирует пользователя, что он плохо предоставляет отчеты. Далее дату последнего отчета я увеличиваю
     * на два дня если увеличенная дата будет равна настоящей дате бот свяжется с волантером и предоставит ему
     * данные на пользователя который плохо заполняет отчеты
     */
//    private void showInfoWhenFailedDeadline(List<Owner> owners) {
//        Long VOLUNTEER_CHAT_ID = 512213990L;
//        LocalDateTime localDateTimeNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
//        owners.forEach(element -> {
//            if (element.getDateOfLastReport().plusMinutes(1).equals(localDateTimeNow)) {
//                telegramBot.execute(new SendMessage(element.getChatId(), "Дорогой усыновитель, мы заметили," +
//                        " что за последние сутки вы предоставляли не подробные отчеты о животном, пожалуйста" +
//                        " отнеситесь серьезно к предоставлению отчетов"));
//            } else if (element.getDateOfLastReport().plusMinutes(2).equals(localDateTimeNow)) {
//                telegramBot.execute(new SendMessage(VOLUNTEER_CHAT_ID, "Пользователь," +
//                        " по имени: " + element.getName() + " id: " + element.getChatId() + " более двух суток не" +
//                        " заполнял отчет, пожалуйста свяжитесь с ним"));
//            }
//        });
//    }

    /**
     * Если волантер решил продлить исп срок овнера, он должен вручную добавить срок продления в бд и
     * поменять статус усыновителя на EXTENDED. Далее метод проверяет у овнеров срок продления и статус,
     * если срок продления больше 0 и статус равен EXTENDED бот информирует овнера о продлении исп срока. Далее метод
     * перезаписывает статус овнера на FINALLY_EXTENDED, чтобы бот повторно не информировал овнера
     */
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
}


