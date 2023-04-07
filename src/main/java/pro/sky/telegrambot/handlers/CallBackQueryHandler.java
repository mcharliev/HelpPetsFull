package pro.sky.telegrambot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import pro.sky.telegrambot.keyboard.InlineKeyboard;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CallBackQueryHandler implements Handler {
    private final TelegramBot telegramBot;

    public CallBackQueryHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        InlineKeyboard infoKeyboard = new InlineKeyboard(telegramBot);
        switch (data) {
            case "Кнопка 1": {
                infoKeyboard.showInfoShelterMenu(chatId);
                break;
            }
            case "Кнопка 2": {
                infoKeyboard.showBureaucraticMenu(chatId);
                break;
            }
            case "Кнопка 3": {
                infoKeyboard.showReportMenu(chatId);
                break;
            }
            case "Кнопка 2.11":
            case "Кнопка 1.5":
            case "Кнопка 3.4":
            case "Кнопка 4": {
//                callVolunteer(chatId);
                infoKeyboard.showVolunteerMenu(chatId);
                break;
            }
            case "Кнопка 1.1": {
                showInfoAboutShelter(chatId);
                break;
            }
            case "Кнопка 1.2": {
                showContactInfoAboutShelter(chatId);
                break;
            }
            case "Кнопка 1.3": {
                showSafetyAdvice(chatId);
                break;
            }
            case "Кнопка 1.4": {
                saveContactDetails(chatId);
                break;
            }
            case "Кнопка 1.6":
            case "Кнопка 2.12":
            case "Кнопка 3.3":
            case "Кнопка 4.1": {
                infoKeyboard.showStartMenu(chatId);
                break;
            }
            case "Кнопка 2.1": {
                showDogDatingRules(chatId);
                break;
            }
            case "Кнопка 2.2": {
                showListOfDocuments(chatId);
                break;
            }
            case "Кнопка 2.3": {
                showTransportationAdvices(chatId);
                break;
            }
            case "Кнопка 2.4": {
                showHomeImprovementTipsForPuppy(chatId);
                break;
            }
            case "Кнопка 2.5": {
                showHomeImprovementTipsForDog(chatId);
                break;
            }
            case "Кнопка 2.6": {
                showHomeImprovementTipsForDogWithDisability(chatId);
                break;
            }
            case "Кнопка 2.7": {
                showDogHandlerAdvices(chatId);
                break;
            }
            case "Кнопка 2.8": {
                showContactsOfDogHandlers(chatId);
                break;
            }
            case "Кнопка 2.9": {
                showReasonsForRefusingToAdoptDog(chatId);
                break;
            }
            case "Кнопка 3.1": {
                showDailyReportForm(chatId);
                break;
            }
            case "Кнопка 3.2": {
                showSendReport(chatId);
                break;
            }
        }
    }

    private void sendTextMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        telegramBot.execute(sendMessage);
    }

    private void showInfoAboutShelter(Long chatId) {
        sendTextMessage(chatId, "Приют Help Pets это место содержания бездомных, потерянных, брошенных " +
                "и больных животных.Тут находятся питомцы, от которых отказались хозяева, найденные на улице " +
                "раненые кошки и собаки. Основаные функции приюта это:" +
                "\nпринимать животных от владельцев или найденных на улице;" +
                "\nсоздать хорошие условия для проживания;" +
                "\nпроводить работу по поиску новых хозяев;" +
                "\nвременно принять животных, сданных владельцами;" +
                "\nприютить больных или травмированных кошек и собак.");
    }

    private void showContactInfoAboutShelter(Long chatId) {
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
    }

    private void showSafetyAdvice(Long chatId) {
        sendTextMessage(chatId, "На территории Приюта для всех посетителей действуют правила и распорядок," +
                " установленные администрацией Приюта: " +
                "\nпроведение фото и видео фиксации без предварительного письменного согласования;" +
                "\nкормить животных кормами и продуктами, как на территории Приюта,;" +
                "\nпосещать блок карантина и изолятор;" +
                "\nбез необходимости находиться вблизи вольеров;" +
                "\nдавать животным самостоятельно какие-либо ветеринарные или медицинские препараты." +
                "\n выгуливать животных без поводка .");
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

    private void showTransportationAdvices(Long chatId) {
        sendTextMessage(chatId, "Если вы собираетесь перевозить питомка в общественном транспорте необходимо это делать " +
                " соблюдая правила перевозки собак в общественном транспорте, при наличии поводка, намордника, проездного" +
                " билета, бокса, клетки или сумки-переноски для маленьких пород, а также необходимых ветеринарных документов " +
                " водители общественного транспорта не имеют права не пустить вас с питомцем в салон.");
    }

    public void showListOfDocuments(Long chatId) {
        sendTextMessage(chatId, "Чтобы взять собаку из приюта, для составления договора и акта " +
                " приема передачи необходим паспорт гражданина Российской Федерации");
    }

    public void showHomeImprovementTipsForPuppy(Long chatId) {
        sendTextMessage(chatId, "Помните, что щенок как маленький ребенок, ему все интересно." +
                "\nПровода, в первую очередь необходимо их спрятать или убрать." +
                "\n Не застилайте пол скользящими тканями, лучше использовать покрытия типа ковролина." +
                "\n Домашние растения. Убирайте их повыше. Если щенок обратит внимание на горшок, вам придётся собирать" +
                "\n землю и песок по всему дому." +
                "\n Избежать порчи мебели можно с помощью специальных средств-антигрызинов или острого перца." +
                "\n Неприятный запах или вкус отпугнет животное");
    }

    public void showHomeImprovementTipsForDog(Long chatId) {
        sendTextMessage(chatId, "Собаки грызут всё, что попадётся на глаза: провода, одежду, обувь, " +
                "бутылки с бытовой химией. Поэтому все шнуры и зарядки убирайте в шкафы." +
                "Кабель от интернета лучше вмонтировать в стену или плинтус либо спрятать в кабель-канал." +
                "Шкафы с вещами должны быть закрыты, а для обуви стоит завести шкафчик." +
                "Убирайте лекарства, жидкости для электронных сигарет — питомец может ими отравиться.");
    }

    public void showHomeImprovementTipsForDogWithDisability(Long chatId) {
        sendTextMessage(chatId, "Полноценная счастливая жизнь собаки с ограниченными возможностями напрямую" +
                " зависит от хозяина. Если он прилагает все усилия, чтобы обеспечить комфортную жизнь питомцу," +
                " то животное легко приспосабливается ко многим вещам и проживает длинную и полноценную жизнь" +
                "По возможности уберите провода, одежду, обувь," +
                "бутылки с бытовой химией. Поэтому все шнуры и зарядки убирайте в шкафы." +
                "Кабель от интернета лучше вмонтировать в стену или плинтус либо спрятать в кабель-канал." +
                "Шкафы с вещами должны быть закрыты, а для обуви стоит завести шкафчик." +
                "Убирайте лекарства, жидкости для электронных сигарет — питомец может ими отравиться.");
    }

    public void showDogHandlerAdvices(Long chatId) {
        sendTextMessage(chatId, "Важно уделить внимание дрессировке и воспитанию необходимо выполнять комплекс" +
                " мероприятий комплекс мероприятий: кормление и уход, физическое развитие, воспитание, обучение и" +
                " тренировка собаки, ветеринарное обслуживание. Воспитанием щенков следует заниматься вскоре после " +
                " рождения, наиболее подходящий возраст – 3,5 — 4 месяца. Дрессировкой и воспитанием собаки можно" +
                " заниматься самостоятельно. ОКД – общий курс дрессировки. Включает в себя обучение собаки основным " +
                " командам («Ко мне!», «Рядом!», «Сидеть!», «Лежать!», «Стоять!», «Фу!», «Место!») Обучение происходит " +
                " на площадке, с участием хозяина, индивидуально или в группе. ");
    }

    public void showContactsOfDogHandlers(Long chatId) {
        sendTextMessage(chatId, "Приют для животных Help Pets сотрудничается с опытными кинологами, которых " +
                " может рекомендовать как профессионалов своего дела. Предоставляем Вам их контакты для дальнейшего " +
                " обрещения: \n 1.Михаийлов Михаил 79998887766 \n 2.Петров Петр 78889996655" +
                " 3.Мишин Алексей 77776665522 4.Петрова Мария 76665552233  ");
    }

    public void showReasonsForRefusingToAdoptDog(Long chatId) {
        sendTextMessage(chatId, "Собаку из приюта не получится забрать подросткам или людям, которые пришли на " +
                " собеседование пьяными или под кайфом. Help Pets также отказывает тем, кто живет на съемных" +
                " квартирах, людям без документов тоже откажут забрать собаку из приюта");
    }

    public void showDailyReportForm(Long chatId) {
        sendTextMessage(chatId, "В данном разделе люди которые забрали" +
                " животное из приюта должны предоставить  информацию о том, как животное чувствует себя" +
                " на новом месте. Отчеты принимаются в виде фотографий и текста. В тексте просим Вас " +
                " предоставить информацию о рационе животного, общее самочувствие, изменение в поведении:" +
                " отказ от старых привычек, приобретение новых. Текстовую информацию отправляйте одним сообщением.");
    }

    public void showSendReport(Long chatId) {
        sendTextMessage(chatId, "Прикрепите пожалуйста фотографии, и напишите информацию согласно форме" +
                " предоставления отчета, и нажмите отправить.");
    }

     /* метод зовет волонтора, в константе записан chatId волонтера, когда пользователь нажимает на кнопку
      позвать волонтера бот высылает волонтеру уведомление чтобы он связался с пользователем*/
    public void callVolunteer(Long chatId) {
        Long VOLUNTEER_CHAT_ID = 512213990L;
        sendTextMessage(VOLUNTEER_CHAT_ID, "Пользователь id: " + chatId +
                " просит связи с волантером, пожалуйста свяжитесь с ним");
    }
}

