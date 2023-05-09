package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.keyboard.Button;
import pro.sky.telegrambot.model.CatOwner;
import pro.sky.telegrambot.model.DogOwner;
import pro.sky.telegrambot.model.UserContext;
import pro.sky.telegrambot.repository.UserContextRepository;
import pro.sky.telegrambot.service.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {
    @Mock
    private TelegramBot telegramBot;
    @Mock
    DogOwnerService dogOwnerService;
    @Mock
    CatOwnerService catOwnerService;
    @Mock
    DogOwnerReportService dogOwnerReportService;
    @Mock
    CatOwnerReportService catOwnerReportService;
    @Mock
    UserContextRepository userContextRepository;
    @Mock
    DogShelterUserService dogShelterUserService;
    @Mock
    CatShelterUserService catShelterUserService;
    @InjectMocks
    TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    public void checkStartMenu() throws Exception {
        Path filePath = Paths.get("src/test/resources/text_update.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "/start");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(1);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button2);
    }

    @Test
    public void checkDogShelterMenu() throws Exception {
        Path filePath = Paths.get("src/test/resources/text_update.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "/start");
        Optional<UserContext> userContext = Optional.of(new UserContext());
        userContext.get().setChatId(123L);
        userContext.get().setDogShelter(true);
        when(userContextRepository.findByChatId(123L)).thenReturn(userContext);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(3);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button1_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button1_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button1_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button1_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button1_5);
    }

    @Test
    public void checkCatShelterMenu() throws Exception {
        Path filePath = Paths.get("src/test/resources/text_update.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "/start");
        Optional<UserContext> userContext = Optional.of(new UserContext());
        userContext.get().setChatId(123L);
        userContext.get().setCatShelter(true);
        when(userContextRepository.findByChatId(123L)).thenReturn(userContext);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(3);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button2_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button2_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button2_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button2_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button2_5);
    }

    @Test
    public void handelContactDetailsOfDogShelterUser() throws Exception {
        Path filePath = Paths.get("src/test/resources/text_update.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "71112223344 Михаил");
        Optional<UserContext> userContext = Optional.of(new UserContext());
        userContext.get().setChatId(123L);
        userContext.get().setDogShelter(true);
        when(userContextRepository.findByChatId(123L)).thenReturn(userContext);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        ArgumentCaptor<String> phoneArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(dogShelterUserService).addUser(
                phoneArgumentCaptor.capture(),
                nameArgumentCaptor.capture());
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Ваша контактная информация " +
                "сохранена, скоро с вами свяжется один" +
                " из наших волонтеров");
    }

    @Test
    public void handelContactDetailsOfCatShelterUser() throws Exception {
        Path filePath = Paths.get("src/test/resources/text_update.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "71112223344 Михаил");
        Optional<UserContext> userContext = Optional.of(new UserContext());
        userContext.get().setChatId(123L);
        userContext.get().setCatShelter(true);
        when(userContextRepository.findByChatId(123L)).thenReturn(userContext);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        ArgumentCaptor<String> phoneArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(catShelterUserService).addUser(
                phoneArgumentCaptor.capture(),
                nameArgumentCaptor.capture());
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Ваша контактная информация " +
                "сохранена, скоро с вами свяжется один" +
                " из наших волонтеров");
    }

    @Test
    public void handelDogOwnerTextReport() throws Exception {
        Path filePath = Paths.get("src/test/resources/text_update.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Собака на новом месте чувствует себя хорошо, кушает с большим аппетитом" +
                " ей очень нравится ее просторная будка и ее игрушки");
        Optional<DogOwner> optDogOwner = Optional.of(new DogOwner());
        optDogOwner.get().setChatId(123L);
        when(dogOwnerService.findDogOwnerByChatId(123L)).thenReturn(optDogOwner);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<DogOwner> dogOwnerArgumentCaptor = ArgumentCaptor.forClass(DogOwner.class);
        ArgumentCaptor<LocalDateTime> dateTimeArgumentCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        Mockito.verify(dogOwnerReportService).saveTextInNewReport(
                stringArgumentCaptor.capture(),dogOwnerArgumentCaptor.capture(),dateTimeArgumentCaptor.capture()
         );
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Вы успешно загрузили текстовый отчет," +
                " пожалуйста не забудьте загрузить фото отчет");
    }
    @Test
    public void handelCatOwnerTextReport() throws Exception {
        Path filePath = Paths.get("src/test/resources/text_update.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кошка на новом месте чувствует себя хорошо, кушает с большим аппетитом" +
                " ей очень нравится ее просторный домик и ее игрушки");
        Optional<CatOwner> optCatOwner = Optional.of(new CatOwner());
        optCatOwner.get().setChatId(123L);
        when(catOwnerService.findCatOwnerByChatId(123L)).thenReturn(optCatOwner);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<CatOwner> catOwnerArgumentCaptor = ArgumentCaptor.forClass(CatOwner.class);
        ArgumentCaptor<LocalDateTime> dateTimeArgumentCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        Mockito.verify(catOwnerReportService).saveTextInNewReport(
                stringArgumentCaptor.capture(),catOwnerArgumentCaptor.capture(),dateTimeArgumentCaptor.capture()
         );
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Вы успешно загрузили текстовый отчет," +
                " пожалуйста не забудьте загрузить фото отчет");
    }

    @Test
    public void handelInvalidMessage() throws Exception {
        Path filePath = Paths.get("src/test/resources/text_update.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Привет");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Команда не распознана");
    }
    @Test
    public void showInfoDogShelterMenu_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 1.1");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(3);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button3_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button3_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button3_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button3_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button3_5);
        Assertions.assertThat(inlineKeyboardButtons[2][1].callbackData()).isEqualTo(Button.button3_6);
    }
    @Test
    public void showInfoCatShelterMenu_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 2.1");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(3);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button7_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button7_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button7_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button7_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button7_5);
        Assertions.assertThat(inlineKeyboardButtons[2][1].callbackData()).isEqualTo(Button.button7_6);
    }
    @Test
    public void showBureaucraticMenuAboutDogs_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 1.2");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(6);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button4_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button4_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button4_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button4_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button4_5);
        Assertions.assertThat(inlineKeyboardButtons[2][1].callbackData()).isEqualTo(Button.button4_6);
        Assertions.assertThat(inlineKeyboardButtons[3][0].callbackData()).isEqualTo(Button.button4_7);
        Assertions.assertThat(inlineKeyboardButtons[3][1].callbackData()).isEqualTo(Button.button4_8);
        Assertions.assertThat(inlineKeyboardButtons[4][0].callbackData()).isEqualTo(Button.button4_9);
        Assertions.assertThat(inlineKeyboardButtons[4][1].callbackData()).isEqualTo(Button.button4_10);
        Assertions.assertThat(inlineKeyboardButtons[5][0].callbackData()).isEqualTo(Button.button4_11);
        Assertions.assertThat(inlineKeyboardButtons[5][1].callbackData()).isEqualTo(Button.button4_12);
    }
    @Test
    public void showBureaucraticMenuAboutCats_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 2.2");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(5);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button6_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button6_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button6_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button6_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button6_5);
        Assertions.assertThat(inlineKeyboardButtons[2][1].callbackData()).isEqualTo(Button.button6_6);
        Assertions.assertThat(inlineKeyboardButtons[3][0].callbackData()).isEqualTo(Button.button6_7);
        Assertions.assertThat(inlineKeyboardButtons[3][1].callbackData()).isEqualTo(Button.button6_8);
        Assertions.assertThat(inlineKeyboardButtons[4][0].callbackData()).isEqualTo(Button.button6_9);
        Assertions.assertThat(inlineKeyboardButtons[4][1].callbackData()).isEqualTo(Button.button6_10);
    }
    @Test
    public void showReportMenu_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 2.3");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button5_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button5_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button5_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button5_4);
    }
    @Test
    public void showInfoAboutShelter_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 3.1");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Приют Help Pets это место содержания бездомных, потерянных, брошенных" +
                        " и больных животных. Тут находятся питомцы, от которых отказались хозяева, найденные на улице" +
                        " Основаные функции приюта это:" +
                        "\nпринимать животных от владельцев или найденных на улице;" +
                        "\nсоздать хорошие условия для проживания;" +
                        "\nпроводить работу по поиску новых хозяев;" +
                        "\nвременно принять животных, сданных владельцами;" +
                        "\nприютить больных или травмированных кошек и собак.");
    }
    @Test
    public void showSafetyAdvice_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 7.3");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("На территории Приюта для всех посетителей действуют правила и распорядок," +
                        "установленные администрацией Приюта: " +
                        "\nпроведение фото и видео фиксации без предварительного письменного согласования;" +
                        "\nкормить животных кормами и продуктами, как на территории приюта;" +
                        "\nпосещать блок карантина и изолятор;" +
                        "\nбез необходимости находиться вблизи вольеров;" +
                        "\nдавать животным самостоятельно какие-либо ветеринарные или медицинские препараты.");
    }
    @Test
    public void saveContactDetails_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 3.4");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Привет, отправь номер телефона и имя " +
                        "в формате 71112223344 Михаил");
    }
    @Test
    public void showDogDatingRules_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 4.1");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Избегайте взгляда глаза в глаза. Можете погладить по бокам, щечкам," +
                        " груди, если она не сопротивляется — по спине. Можете угостить" +
                        " лакомством на открытой ладони. В целом, будьте доброжелательны.");
    }
    @Test
    public void showCatDatingRules_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 6.1");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Кошки – независимые животные с чувством собственного достоинства, предпочитают" +
                        " быть с человеком на равных. Поэтому знакомиться или здороваться с ней нужно на кошачьем языке." +
                        " Между собой знакомство у кошачьих начинается со взаимного обнюхивания, поэтому при первой встрече," +
                        " протяните кошке руку, чтобы та её обнюхала, поняла, что опасности здесь нет. Если всё пройдёт гладко," +
                        " кошка либо потрётся о вашу руку мордочкой, либо повернётся хвостом и можно будет почесать её спинку.");
    }
    @Test
    public void showTransportationAdvices_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 4.3");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Если вы собираетесь перевозить питомца в общественном транспорте необходимо это делать " +
                        " соблюдая правила перевозки животных в общественном транспорте необходимо наличие бокса, клетки или " +
                        " сумки-переноски для животных, а также необходимых ветеринарных документов " +
                        " водители общественного транспорта не имеют права не пустить вас с питомцем в салон.");
    }
    @Test
    public void showListOfDocuments_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 4.2");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Чтобы взять животное из приюта, для составления договора и акта " +
                        " приема передачи необходим паспорт гражданина Российской Федерации");
    }
    @Test
    public void showHomeImprovementTipsForPuppy_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 4.4");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Помните, что щенок как маленький ребенок, ему все интересно." +
                        "\nПровода, в первую очередь необходимо их спрятать или убрать." +
                        "\n Не застилайте пол скользящими тканями, лучше использовать покрытия типа ковролина." +
                        "\n Домашние растения. Убирайте их повыше. Если щенок обратит внимание на горшок, вам придётся собирать" +
                        "\n землю и песок по всему дому." +
                        "\n Избежать порчи мебели можно с помощью специальных средств-антигрызинов или острого перца." +
                        "\n Неприятный запах или вкус отпугнет животное");
    }
    @Test
    public void showHomeImprovementTipsForKitty_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 6.4");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Маленькие неприятности можно с легкостью предупредить, убрав все хрупкие " +
                        "предметы туда, где их не найдёт котёнок. Следует чем-нибудь закрыть узкие щели между мебелью и " +
                        "стеной, откуда малыша трудно будет достать. Заигравшись, котёнок может проглотить мелкие предметы " +
                        "(скрепки, пуговки). Их лучше убрать подальше. Лучше всего заводить котёнка в то время, когда вы " +
                        "сможете посвятить ему себя в полной мере (выходные, отпуск). Ведь первые 2-3 дня на новом месте" +
                        " он адаптируется, и желательно быть рядом, чтобы проследить походы в туалет, вовремя дать " +
                        "покушать, поиграть и погладить, уложить спать.");
    }
    @Test
    public void showHomeImprovementTipsForDog_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 4.5");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Собаки грызут всё, что попадётся на глаза: провода, одежду, обувь, " +
                        "бутылки с бытовой химией. Поэтому все шнуры и зарядки убирайте в шкафы." +
                        "Кабель от интернета лучше вмонтировать в стену или плинтус либо спрятать в кабель-канал." +
                        "Шкафы с вещами должны быть закрыты, а для обуви стоит завести шкафчик." +
                        "Убирайте лекарства, жидкости для электронных сигарет — питомец может ими отравиться.");
    }
    @Test
    public void showHomeImprovementTipsForCat_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 6.5");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Итак, вы выбрали кошку. Теперь ваша задача — обустроить пространство, в " +
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
    @Test
    public void showHomeImprovementTipsForPetWithDisability_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 4.6");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Полноценная счастливая жизнь животного с ограниченными возможностями напрямую" +
                        " зависит от хозяина. Если он прилагает все усилия, чтобы обеспечить комфортную жизнь питомцу," +
                        " то животное легко приспосабливается ко многим вещам и проживает длинную и полноценную жизнь" +
                        " По возможности уберите провода, одежду, обувь," +
                        " бутылки с бытовой химией. Поэтому все шнуры и зарядки убирайте в шкафы." +
                        " Кабель от интернета лучше вмонтировать в стену или плинтус либо спрятать в кабель-канал." +
                        " Шкафы с вещами должны быть закрыты, а для обуви стоит завести шкафчик." +
                        " Убирайте лекарства, жидкости для электронных сигарет — питомец может ими отравиться.");
    }
    @Test
    public void showDogHandlerAdvices_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 4.7");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Важно уделить внимание дрессировке и воспитанию необходимо выполнять комплекс" +
                        " мероприятий комплекс мероприятий: кормление и уход, физическое развитие, воспитание, обучение и" +
                        " тренировка собаки, ветеринарное обслуживание. Воспитанием щенков следует заниматься вскоре после " +
                        " рождения, наиболее подходящий возраст – 3,5 — 4 месяца. Дрессировкой и воспитанием собаки можно" +
                        " заниматься самостоятельно. ОКД – общий курс дрессировки. Включает в себя обучение собаки основным " +
                        " командам («Ко мне!», «Рядом!», «Сидеть!», «Лежать!», «Стоять!», «Фу!», «Место!») Обучение происходит " +
                        " на площадке, с участием хозяина, индивидуально или в группе. ");
    }
 @Test
    public void showContactsOfDogHandlers_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 4.8");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Приют для животных Help Pets сотрудничается с опытными кинологами, которых " +
                        " может рекомендовать как профессионалов своего дела. Предоставляем Вам их контакты для дальнейшего " +
                        " обрещения: \n 1.Михаийлов Михаил 79998887766 \n 2.Петров Петр 78889996655" +
                        " 3.Мишин Алексей 77776665522 4.Петрова Мария 76665552233  ");
    }
    @Test
    public void showReasonsForRefusingToAdoptDog_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 4.9");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Питомца из приюта не получится забрать несовершеннолетним или людям, " +
                        "которые пришли на собеседование пьяными или под кайфом. Help Pets также отказывает тем," +
                        " кто живет на съемных +квартирах, людям без документов тоже откажут забрать животное" +
                        " из приюта");
    }
    @Test
    public void showDailyReportForm_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 5.1");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("В данном разделе люди которые забрали" +
                        " животное из приюта должны предоставить  информацию о том, как животное чувствует себя" +
                        " на новом месте. Отчеты принимаются в виде фотографий и текста. В тексте просим Вас " +
                        " предоставить информацию о рационе животного, общее самочувствие, изменение в поведении:" +
                        " отказ от старых привычек, приобретение новых. Текстовую информацию отправляйте одним сообщением.");
    }
 @Test
    public void showSendReport_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 5.2");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Прикрепите пожалуйста фотографии, и напишите информацию согласно форме" +
                        " предоставления отчета, и нажмите отправить.");
    }
    @Test
    public void showCatShelterMenu_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 2");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(3);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button2_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button2_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button2_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button2_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button2_5);
    }
    @Test
    public void showDogShelterMenu_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 1");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                ((InlineKeyboardMarkup) actual.getParameters().get("reply_markup")).inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(3);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button1_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button1_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button1_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button1_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button1_5);
    }
    @Test
    public void callVolunteer_Test() throws Exception {
        Path filePath = Paths.get("src/test/resources/callback_data.json");
        String json = Files.readString(filePath);
        Update update = getUpdate(json, "Кнопка 1.4");
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(512213990L);
        Assertions.assertThat(actual.getParameters().get("text"))
                .isEqualTo("Пользователь id: 123 просит связи с волантером, пожалуйста свяжитесь с ним");
    }




    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }
}