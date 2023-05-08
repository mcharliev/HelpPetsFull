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

    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }
}