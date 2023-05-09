package pro.sky.telegrambot.keyboard;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class InlineKeyboardTest {

    @Test
    void chooseShelterMenuTest() {
        InlineKeyboardButton button1 = new InlineKeyboardButton("Приют для собак");
        button1.callbackData(Button.button1);
        InlineKeyboardButton button2 = new InlineKeyboardButton("Приют для кошек");
        button2.callbackData(Button.button2);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(button1, button2);

        InlineKeyboardButton[][] inlineKeyboardButtons =
                inlineKeyboardMarkup.inlineKeyboard();

        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(1);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button2);
    }


    @Test
    void showDogShelterMenuTest() {
        InlineKeyboardButton button1_1 = new InlineKeyboardButton("Узнать информацию о собачьем приюте");
        button1_1.callbackData(Button.button1_1);
        InlineKeyboardButton button1_2 = new InlineKeyboardButton("Как взять собаку из приюта");
        button1_2.callbackData(Button.button1_2);
        InlineKeyboardButton button1_3 = new InlineKeyboardButton("Прислать отчет о собаке");
        button1_3.callbackData(Button.button1_3);
        InlineKeyboardButton button1_4 = new InlineKeyboardButton("Позвать волонтера");
        button1_4.callbackData(Button.button1_4);
        InlineKeyboardButton button1_5 = new InlineKeyboardButton("Вернуться к выбору приюта");
        button1_5.callbackData(Button.button1_5);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(button1_1, button1_2);
        inlineKeyboardMarkup.addRow(button1_3, button1_4);
        inlineKeyboardMarkup.addRow(button1_5);

        InlineKeyboardButton[][] inlineKeyboardButtons =
                inlineKeyboardMarkup.inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(3);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button1_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button1_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button1_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button1_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button1_5);
    }

    @Test
    void showCatShelterMenuTest() {
        InlineKeyboardButton button2_1 = new InlineKeyboardButton("Узнать информацию о кошачем приюте");
        button2_1.callbackData(Button.button2_1);
        InlineKeyboardButton button2_2 = new InlineKeyboardButton("Как взять кошку из приюта");
        button2_2.callbackData(Button.button2_2);
        InlineKeyboardButton button2_3 = new InlineKeyboardButton("Прислать отчет о кошке");
        button2_3.callbackData(Button.button2_3);
        InlineKeyboardButton button2_4 = new InlineKeyboardButton("Позвать волонтера");
        button2_4.callbackData(Button.button2_4);
        InlineKeyboardButton button2_5 = new InlineKeyboardButton("Вернуться к выбору приюта");
        button2_5.callbackData(Button.button2_5);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(button2_1, button2_2);
        inlineKeyboardMarkup.addRow(button2_3, button2_4);
        inlineKeyboardMarkup.addRow(button2_5);

        InlineKeyboardButton[][] inlineKeyboardButtons =
                inlineKeyboardMarkup.inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(3);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button2_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button2_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button2_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button2_4);
        Assertions.assertThat(inlineKeyboardButtons[2][0].callbackData()).isEqualTo(Button.button2_5);
    }

    @Test
    void showInfoDogShelterMenuTest() {
        InlineKeyboardButton button3_1 = new InlineKeyboardButton("Подробная информация о собачьем приюте");
        button3_1.callbackData(Button.button3_1);
        InlineKeyboardButton button3_2 = new InlineKeyboardButton("Контактая информация собачьего приюта");
        button3_2.callbackData(Button.button3_2);
        InlineKeyboardButton button3_3 = new InlineKeyboardButton("Рекомендации" +
                " о технике безопасности на территории собачьего приюта");
        button3_3.callbackData(Button.button3_3);
        InlineKeyboardButton button3_4 = new InlineKeyboardButton("Записать " +
                "контактные данные для связи собачьего приюта");
        button3_4.callbackData(Button.button3_4);
        InlineKeyboardButton button3_5 = new InlineKeyboardButton("Позвать волонтера собачьего приюта");
        button3_5.callbackData(Button.button3_5);
        InlineKeyboardButton button3_6 = new InlineKeyboardButton("Вернуться к выбору приюта");
        button3_6.callbackData(Button.button3_6);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(button3_1, button3_2);
        inlineKeyboardMarkup.addRow(button3_3, button3_4);
        inlineKeyboardMarkup.addRow(button3_5, button3_6);

        InlineKeyboardButton[][] inlineKeyboardButtons =
                inlineKeyboardMarkup.inlineKeyboard();
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
    void showInfoCatShelterMenuTest() {
        InlineKeyboardButton button3_1 = new InlineKeyboardButton("Подробная информация о кошачьем приюте");
        button3_1.callbackData(Button.button7_1);
        InlineKeyboardButton button3_2 = new InlineKeyboardButton("Контактая информация кошачьего приюта");
        button3_2.callbackData(Button.button7_2);
        InlineKeyboardButton button3_3 = new InlineKeyboardButton("Рекомендации" +
                " о технике безопасности на территории кошачьего приюта");
        button3_3.callbackData(Button.button7_3);
        InlineKeyboardButton button3_4 = new InlineKeyboardButton("Записать " +
                "контактные данные для связи кошачьего приюта");
        button3_4.callbackData(Button.button7_4);
        InlineKeyboardButton button3_5 = new InlineKeyboardButton("Позвать волонтера кошачьего приюта");
        button3_5.callbackData(Button.button7_5);
        InlineKeyboardButton button3_6 = new InlineKeyboardButton("Вернуться к выбору приюта");
        button3_6.callbackData(Button.button7_6);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(button3_1, button3_2);
        inlineKeyboardMarkup.addRow(button3_3, button3_4);
        inlineKeyboardMarkup.addRow(button3_5, button3_6);
        InlineKeyboardButton[][] inlineKeyboardButtons =
                inlineKeyboardMarkup.inlineKeyboard();
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
    void showBureaucraticMenuAboutDogsTest() {
        InlineKeyboardButton button4_1 = new InlineKeyboardButton("Правила знакомства с собакой");
        button4_1.callbackData(Button.button4_1);
        InlineKeyboardButton button4_2 = new InlineKeyboardButton("Присок документов, чтобы взять собаку");
        button4_2.callbackData(Button.button4_2);
        InlineKeyboardButton button4_3 = new InlineKeyboardButton("Рекомендации по транспортировке");
        button4_3.callbackData(Button.button4_3);
        InlineKeyboardButton button4_4 = new InlineKeyboardButton("Рекомендации по обустройству дома" +
                " для щенка");
        button4_4.callbackData(Button.button4_4);
        InlineKeyboardButton button4_5 = new InlineKeyboardButton("Рекомендации по обустройству дома" +
                " для взрослой собаки");
        button4_5.callbackData(Button.button4_5);
        InlineKeyboardButton button4_6 = new InlineKeyboardButton("Рекомендации по обустройству дома" +
                " для собаки с ограниченными возможностями");
        button4_6.callbackData(Button.button4_6);
        InlineKeyboardButton button4_7 = new InlineKeyboardButton("Советы кинолога по первичному " +
                "общению с собакой");
        button4_7.callbackData(Button.button4_7);
        InlineKeyboardButton button4_8 = new InlineKeyboardButton("Рекомендации по проверенным кинологам " +
                "для дальнейшего обращения к ним");
        button4_8.callbackData(Button.button4_8);
        InlineKeyboardButton button4_9 = new InlineKeyboardButton("Причины согласно которым могут " +
                "отказать забрать собаку из приюта");
        button4_9.callbackData(Button.button4_9);
        InlineKeyboardButton button4_10 = new InlineKeyboardButton("Записать " +
                "контактные данные для связи");
        button4_10.callbackData(Button.button4_10);
        InlineKeyboardButton button4_11 = new InlineKeyboardButton("Позвать волонтера");
        button4_11.callbackData(Button.button4_11);
        InlineKeyboardButton button4_12 = new InlineKeyboardButton("Вернуться к выбору приюта");
        button4_12.callbackData(Button.button4_12);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(button4_1, button4_2);
        inlineKeyboardMarkup.addRow(button4_3, button4_4);
        inlineKeyboardMarkup.addRow(button4_5, button4_6);
        inlineKeyboardMarkup.addRow(button4_7, button4_8);
        inlineKeyboardMarkup.addRow(button4_9, button4_10);
        inlineKeyboardMarkup.addRow(button4_11, button4_12);

        InlineKeyboardButton[][] inlineKeyboardButtons =
                inlineKeyboardMarkup.inlineKeyboard();
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
    void showReportMenuTest() {
        InlineKeyboardButton button5_1 = new InlineKeyboardButton("Прислать форму ежедневного отчета");
        button5_1.callbackData(Button.button5_1);
        InlineKeyboardButton button5_2 = new InlineKeyboardButton("Отправить отчет");
        button5_2.callbackData(Button.button5_2);
        InlineKeyboardButton button5_3 = new InlineKeyboardButton("Вернуться к выбору приюта");
        button5_3.callbackData(Button.button5_3);
        InlineKeyboardButton button5_4 = new InlineKeyboardButton("Позвать волонтера");
        button5_4.callbackData(Button.button5_4);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(button5_1, button5_2);
        inlineKeyboardMarkup.addRow(button5_3, button5_4);

        InlineKeyboardButton[][] inlineKeyboardButtons =
                inlineKeyboardMarkup.inlineKeyboard();
        Assertions.assertThat(inlineKeyboardButtons.length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        Assertions.assertThat(inlineKeyboardButtons[0][0].callbackData()).isEqualTo(Button.button5_1);
        Assertions.assertThat(inlineKeyboardButtons[0][1].callbackData()).isEqualTo(Button.button5_2);
        Assertions.assertThat(inlineKeyboardButtons[1][0].callbackData()).isEqualTo(Button.button5_3);
        Assertions.assertThat(inlineKeyboardButtons[1][1].callbackData()).isEqualTo(Button.button5_4);

    }

    @Test
    void showBureaucraticMenuAboutCatsTest() {
        InlineKeyboardButton button6_1 = new InlineKeyboardButton("Правила знакомства с кошкой");
        button6_1.callbackData(Button.button6_1);
        InlineKeyboardButton button6_2 = new InlineKeyboardButton("Присок документов, чтобы взять кошку");
        button6_2.callbackData(Button.button6_2);
        InlineKeyboardButton button6_3 = new InlineKeyboardButton("Рекомендации по транспортировке");
        button6_3.callbackData(Button.button6_3);
        InlineKeyboardButton button6_4 = new InlineKeyboardButton("Рекомендации по обустройству дома" +
                " для котенка");
        button6_4.callbackData(Button.button6_4);
        InlineKeyboardButton button6_5 = new InlineKeyboardButton("Рекомендации по обустройству дома" +
                " для взрослой кошки");
        button6_5.callbackData(Button.button6_5);
        InlineKeyboardButton button6_6 = new InlineKeyboardButton("Рекомендации по обустройству дома" +
                " для кошки с ограниченными возможностями");
        button6_6.callbackData(Button.button6_6);
        InlineKeyboardButton button6_7 = new InlineKeyboardButton("Причины согласно которым могут " +
                "отказать забрать кошку из приюта");
        button6_7.callbackData(Button.button6_7);
        InlineKeyboardButton button6_8 = new InlineKeyboardButton("Записать " +
                " контактные данные для связи");
        button6_8.callbackData(Button.button6_8);
        InlineKeyboardButton button6_9 = new InlineKeyboardButton("Позвать волонтера");
        button6_9.callbackData(Button.button6_9);
        InlineKeyboardButton button6_10 = new InlineKeyboardButton("Вернуться к выбору приюта");
        button6_10.callbackData(Button.button6_10);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(button6_1, button6_2);
        inlineKeyboardMarkup.addRow(button6_3, button6_4);
        inlineKeyboardMarkup.addRow(button6_5, button6_6);
        inlineKeyboardMarkup.addRow(button6_7, button6_8);
        inlineKeyboardMarkup.addRow(button6_9, button6_10);

        InlineKeyboardButton[][] inlineKeyboardButtons =
                inlineKeyboardMarkup.inlineKeyboard();
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
}