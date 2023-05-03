package pro.sky.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import pro.sky.telegrambot.keyboard.Button;
import pro.sky.telegrambot.keyboard.InlineKeyboard;
import pro.sky.telegrambot.model.UserContext;
import pro.sky.telegrambot.repository.UserContextRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class CallBackQueryHandler implements Handler {
    private final TelegramBot telegramBot;
    private final UserContextRepository userContextRepository;

    public CallBackQueryHandler(TelegramBot telegramBot,
                                UserContextRepository userContextRepository) {
        this.telegramBot = telegramBot;
        this.userContextRepository = userContextRepository;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        InlineKeyboard infoKeyboard = new InlineKeyboard(telegramBot);
        System.out.println("Нажата " + callbackQuery.data());
        switch (data) {
            case Button.button1_5:
            case Button.button2_5:
            case Button.button3_6:
            case Button.button4_12:
            case Button.button5_3:
            case Button.button7_6:
            case Button.button6_10: {
                infoKeyboard.chooseShelterMenu(chatId);
                break;
            }
            case Button.button1: {
                infoKeyboard.showDogShelterMenu(chatId);
                chooseDogShelter(chatId);
                break;
            }
            case Button.button2: {
                infoKeyboard.showCatShelterMenu(chatId);
                chooseCatShelter(chatId);
                break;
            }
            case Button.button1_1:{
                infoKeyboard.showInfoDogShelterMenu(chatId);
                break;
            }
            case Button.button1_2: {
                infoKeyboard.showBureaucraticMenuAboutDogs(chatId);
                break;
            }
            case Button.button1_3:
            case Button.button2_3: {
                infoKeyboard.showReportMenu(chatId);
                break;
            }
            case Button.button1_4:
            case Button.button2_4:
            case Button.button3_5:
            case Button.button5_4:
            case Button.button6_9:
            case Button.button7_5: {
                callVolunteer(chatId);
                break;
            }
            case Button.button2_1:{
                infoKeyboard.showInfoCatShelterMenu(chatId);
                break;
            }
            case Button.button2_2: {
                infoKeyboard.showBureaucraticMenuAboutCats(chatId);
                break;
            }
            case Button.button3_1:
            case Button.button7_1: {
                showInfoAboutShelter(chatId);
                break;
            }
            case Button.button3_2: {
                showContactInfoAboutDogShelter(chatId);
                break;
            }
            case Button.button3_3:
            case Button.button7_3: {
                showSafetyAdvice(chatId);
                break;
            }
            case Button.button3_4:
            case Button.button6_8:
            case Button.button7_4:
            case Button.button4_10: {
                saveContactDetails(chatId);
                break;
            }
            case Button.button4_1: {
                showDogDatingRules(chatId);
                break;
            }
            case Button.button4_2:
            case Button.button6_2: {
                showListOfDocuments(chatId);
                break;
            }
            case Button.button4_3:
            case Button.button6_3: {
                showTransportationAdvices(chatId);
                break;
            }
            case Button.button4_4: {
                showHomeImprovementTipsForPuppy(chatId);
                break;
            }
            case Button.button4_5: {
                showHomeImprovementTipsForDog(chatId);
                break;
            }
            case Button.button6_6:
            case Button.button4_6: {
                showHomeImprovementTipsForPetWithDisability(chatId);
                break;
            }
            case Button.button4_7: {
                showDogHandlerAdvices(chatId);
                break;
            }
            case Button.button4_8: {
                showContactsOfDogHandlers(chatId);
                break;
            }
            case Button.button6_7:
            case Button.button4_9: {
                showReasonsForRefusingToAdoptDog(chatId);
                break;
            }
            case Button.button5_1: {
                showDailyReportForm(chatId);
                break;
            }
            case Button.button5_2: {
                showSendReport(chatId);
                break;
            }
            case Button.button6_1: {
                showCatDatingRules(chatId);
                break;
            }
            case Button.button6_4: {
                showHomeImprovementTipsForKitty(chatId);
                break;
            }
            case Button.button6_5: {
                showHomeImprovementTipsForCat(chatId);
                break;
            }
            case Button.button7_2: {
                showContactInfoAboutCatShelter(chatId);
                break;
            }
        }
    }

    private void sendTextMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        telegramBot.execute(sendMessage);
    }

    private void showInfoAboutShelter(Long chatId) {
        sendTextMessage(chatId, "Приют Help Pets это место содержания бездомных, потерянных, брошенных" +
                " и больных животных. Тут находятся питомцы, от которых отказались хозяева, найденные на улице" +
                " Основаные функции приюта это:" +
                "\nпринимать животных от владельцев или найденных на улице;" +
                "\nсоздать хорошие условия для проживания;" +
                "\nпроводить работу по поиску новых хозяев;" +
                "\nвременно принять животных, сданных владельцами;" +
                "\nприютить больных или травмированных кошек и собак.");
    }

    private void showContactInfoAboutDogShelter(Long chatId) {
        try {
            byte[] drivingDirection = Files.readAllBytes(
                    Paths.get(CallBackQueryHandler.class.getResource("/drivingDirection.jpg").toURI()));
            SendPhoto sendPhoto = new SendPhoto(chatId, drivingDirection);
            sendTextMessage(chatId, "Часы работы приюта Help Pets с 9:00 до 19:00 без выходных," +
                    " приют расположен по адресу: Зубовский бульвар д.17 с.3");
            telegramBot.execute(sendPhoto);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }  private void showContactInfoAboutCatShelter(Long chatId) {
        try {
            byte[] drivingDirection = Files.readAllBytes(
                    Paths.get(CallBackQueryHandler.class.getResource("/drivingDirection2.png").toURI()));
            SendPhoto sendPhoto = new SendPhoto(chatId, drivingDirection);
            sendTextMessage(chatId, "Часы работы кошачьего приюта Help Pets с 9:00 до 19:00 без выходных," +
                    " приют расположен по адресу: Лесная улица д.38");
            telegramBot.execute(sendPhoto);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void showSafetyAdvice(Long chatId) {
        sendTextMessage(chatId, "На территории Приюта для всех посетителей действуют правила и распорядок," +
                "установленные администрацией Приюта: " +
                "\nпроведение фото и видео фиксации без предварительного письменного согласования;" +
                "\nкормить животных кормами и продуктами, как на территории приюта;" +
                "\nпосещать блок карантина и изолятор;" +
                "\nбез необходимости находиться вблизи вольеров;" +
                "\nдавать животным самостоятельно какие-либо ветеринарные или медицинские препараты.");
    }

    private void saveContactDetails(Long chatId) {
        sendTextMessage(chatId, "Привет, отправь номер телефона и имя " +
                "в формате 71112223344 Михаил");

    }

    private void showDogDatingRules(Long chatId) {
        sendTextMessage(chatId, "Избегайте взгляда глаза в глаза. Можете погладить по бокам, щечкам," +
                " груди, если она не сопротивляется — по спине. Можете угостить" +
                " лакомством на открытой ладони. В целом, будьте доброжелательны.");
    }

    private void showCatDatingRules(Long chatId) {
        sendTextMessage(chatId, "Кошки – независимые животные с чувством собственного достоинства, предпочитают" +
                " быть с человеком на равных. Поэтому знакомиться или здороваться с ней нужно на кошачьем языке." +
                " Между собой знакомство у кошачьих начинается со взаимного обнюхивания, поэтому при первой встрече," +
                " протяните кошке руку, чтобы та её обнюхала, поняла, что опасности здесь нет. Если всё пройдёт гладко," +
                " кошка либо потрётся о вашу руку мордочкой, либо повернётся хвостом и можно будет почесать её спинку.");
    }

    private void showTransportationAdvices(Long chatId) {
        sendTextMessage(chatId, "Если вы собираетесь перевозить питомца в общественном транспорте необходимо это делать " +
                " соблюдая правила перевозки животных в общественном транспорте необходимо наличие бокса, клетки или " +
                " сумки-переноски для животных, а также необходимых ветеринарных документов " +
                " водители общественного транспорта не имеют права не пустить вас с питомцем в салон.");
    }

    private void showListOfDocuments(Long chatId) {
        sendTextMessage(chatId, "Чтобы взять животное из приюта, для составления договора и акта " +
                " приема передачи необходим паспорт гражданина Российской Федерации");
    }

    private void showHomeImprovementTipsForPuppy(Long chatId) {
        sendTextMessage(chatId, "Помните, что щенок как маленький ребенок, ему все интересно." +
                "\nПровода, в первую очередь необходимо их спрятать или убрать." +
                "\n Не застилайте пол скользящими тканями, лучше использовать покрытия типа ковролина." +
                "\n Домашние растения. Убирайте их повыше. Если щенок обратит внимание на горшок, вам придётся собирать" +
                "\n землю и песок по всему дому." +
                "\n Избежать порчи мебели можно с помощью специальных средств-антигрызинов или острого перца." +
                "\n Неприятный запах или вкус отпугнет животное");
    }

    private void showHomeImprovementTipsForKitty(Long chatId) {
        sendTextMessage(chatId, "Маленькие неприятности можно с легкостью предупредить, убрав все хрупкие " +
                "предметы туда, где их не найдёт котёнок. Следует чем-нибудь закрыть узкие щели между мебелью и " +
                "стеной, откуда малыша трудно будет достать. Заигравшись, котёнок может проглотить мелкие предметы " +
                "(скрепки, пуговки). Их лучше убрать подальше. Лучше всего заводить котёнка в то время, когда вы " +
                "сможете посвятить ему себя в полной мере (выходные, отпуск). Ведь первые 2-3 дня на новом месте" +
                " он адаптируется, и желательно быть рядом, чтобы проследить походы в туалет, вовремя дать " +
                "покушать, поиграть и погладить, уложить спать.");
    }

    private void showHomeImprovementTipsForDog(Long chatId) {
        sendTextMessage(chatId, "Собаки грызут всё, что попадётся на глаза: провода, одежду, обувь, " +
                "бутылки с бытовой химией. Поэтому все шнуры и зарядки убирайте в шкафы." +
                "Кабель от интернета лучше вмонтировать в стену или плинтус либо спрятать в кабель-канал." +
                "Шкафы с вещами должны быть закрыты, а для обуви стоит завести шкафчик." +
                "Убирайте лекарства, жидкости для электронных сигарет — питомец может ими отравиться.");
    }

    private void showHomeImprovementTipsForCat(Long chatId) {
        sendTextMessage(chatId, "Итак, вы выбрали кошку. Теперь ваша задача — обустроить пространство, в " +
                " котором она будет жить. Обязательно учитывайте, что кошки очень любопытны. Нужно хорошо знать их " +
                " повадки, желания и потребности, чтобы создать в доме не только комфортную, но и безопасную для" +
                " них обстановку.Чтобы кошка легче привыкала к дому, ей должно быть в нем уютно. Для этого нужно " +
                " обустроить для нее места для игр и сна, не забывая о безопасности.\n" +
                " Какие меры предосторожности стоит соблюсти?\n" +
                " Уберите провода и шнуры (их можно спрятать в специальный короб или под плинтус).\n" +
                " Никогда не оставляйте окна, входную и балконную двери открытыми.\n" +
                " Уберите подальше иголки и нитки, елочные украшения, бьющиеся и хрупкие предметы, бытовую химию.\n" +
                " Не оставляйте включенными плиту и утюг.\n" +
                " Перед тем как включать стиральную машинку, проверяйте, не забралась ли кошка внутрь барабана.\n" +
                " Избавьтесь от ядовитых для кошек растений (среди них цикламен, плющ, шеффлера и другие).");
    }

    private void showHomeImprovementTipsForPetWithDisability(Long chatId) {
        sendTextMessage(chatId, "Полноценная счастливая жизнь животного с ограниченными возможностями напрямую" +
                " зависит от хозяина. Если он прилагает все усилия, чтобы обеспечить комфортную жизнь питомцу," +
                " то животное легко приспосабливается ко многим вещам и проживает длинную и полноценную жизнь" +
                " По возможности уберите провода, одежду, обувь," +
                " бутылки с бытовой химией. Поэтому все шнуры и зарядки убирайте в шкафы." +
                " Кабель от интернета лучше вмонтировать в стену или плинтус либо спрятать в кабель-канал." +
                " Шкафы с вещами должны быть закрыты, а для обуви стоит завести шкафчик." +
                " Убирайте лекарства, жидкости для электронных сигарет — питомец может ими отравиться.");
    }

    private void showDogHandlerAdvices(Long chatId) {
        sendTextMessage(chatId, "Важно уделить внимание дрессировке и воспитанию необходимо выполнять комплекс" +
                " мероприятий комплекс мероприятий: кормление и уход, физическое развитие, воспитание, обучение и" +
                " тренировка собаки, ветеринарное обслуживание. Воспитанием щенков следует заниматься вскоре после " +
                " рождения, наиболее подходящий возраст – 3,5 — 4 месяца. Дрессировкой и воспитанием собаки можно" +
                " заниматься самостоятельно. ОКД – общий курс дрессировки. Включает в себя обучение собаки основным " +
                " командам («Ко мне!», «Рядом!», «Сидеть!», «Лежать!», «Стоять!», «Фу!», «Место!») Обучение происходит " +
                " на площадке, с участием хозяина, индивидуально или в группе. ");
    }

    private void showContactsOfDogHandlers(Long chatId) {
        sendTextMessage(chatId, "Приют для животных Help Pets сотрудничается с опытными кинологами, которых " +
                " может рекомендовать как профессионалов своего дела. Предоставляем Вам их контакты для дальнейшего " +
                " обрещения: \n 1.Михаийлов Михаил 79998887766 \n 2.Петров Петр 78889996655" +
                " 3.Мишин Алексей 77776665522 4.Петрова Мария 76665552233  ");
    }

    private void showReasonsForRefusingToAdoptDog(Long chatId) {
        sendTextMessage(chatId, "Питомца из приюта не получится забрать несовершеннолетним или людям, " +
                "которые пришли на собеседование пьяными или под кайфом. Help Pets также отказывает тем," +
                " кто живет на съемных +квартирах, людям без документов тоже откажут забрать животное" +
                " из приюта");
    }

    private void showDailyReportForm(Long chatId) {
        sendTextMessage(chatId, "В данном разделе люди которые забрали" +
                " животное из приюта должны предоставить  информацию о том, как животное чувствует себя" +
                " на новом месте. Отчеты принимаются в виде фотографий и текста. В тексте просим Вас " +
                " предоставить информацию о рационе животного, общее самочувствие, изменение в поведении:" +
                " отказ от старых привычек, приобретение новых. Текстовую информацию отправляйте одним сообщением.");
    }

    private void showSendReport(Long chatId) {
        sendTextMessage(chatId, "Прикрепите пожалуйста фотографии, и напишите информацию согласно форме" +
                " предоставления отчета, и нажмите отправить.");
    }

    /* метод зовет волонтора, в константе записан chatId волонтера, когда пользователь нажимает на кнопку
     позвать волонтера бот высылает волонтеру уведомление чтобы он связался с пользователем*/
    private void callVolunteer(Long chatId) {
        Long VOLUNTEER_CHAT_ID = 512213990L;
        sendTextMessage(VOLUNTEER_CHAT_ID, "Пользователь id: " + chatId +
                " просит связи с волантером, пожалуйста свяжитесь с ним");
    }

    private void chooseDogShelter(Long chatId) {
        UserContext userContext = new UserContext();
        userContext.setChatId(chatId);
        userContext.setDogShelter(true);
        userContextRepository.save(userContext);

    }

    private void chooseCatShelter(Long chatId) {
        UserContext userContext = new UserContext();
        userContext.setChatId(chatId);
        userContext.setCatShelter(true);
        userContextRepository.save(userContext);
    }
}

