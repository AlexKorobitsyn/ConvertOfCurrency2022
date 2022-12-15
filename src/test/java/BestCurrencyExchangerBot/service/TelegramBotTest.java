package BestCurrencyExchangerBot.service;
import BestCurrencyExchangerBot.config.BotConfig;
import BestCurrencyExchangerBot.service.User;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TelegramBotTest {

    @Test
    void testOurScenario() throws Exception {
        BotConfig config = new BotConfig();
        User testUser = new User();
        TelegramBot telegramBot = new TelegramBot(config);
        String message1 = "/start";
        ArrayList<SendMessage> arrayListOfAnswers1 = telegramBot.ourScenario(testUser, message1);
        assertEquals("Choose City", testUser.getStep());
        for(SendMessage message:arrayListOfAnswers1)
        {
            assertEquals("Привет, null, рады тебя видеть!\n", message.getText());
            break;
        }
        String message2 = "London";
        ArrayList<SendMessage> arrayListOfAnswers2 = telegramBot.ourScenario(testUser, message2);
        assertEquals("Запускаю поиск банков в выбранном городе, ожидайте...", arrayListOfAnswers2.get(0).getText());
        assertEquals("Список найденных банков с их курсами:", arrayListOfAnswers2.get(1).getText());
        assertEquals("VTB\n" +
                "EUR: sell: 69.5 buy: 75.0 \n" +
                "USD: sell: 59.5 buy: 66.0 \n" +
                "RUB: sell: 1.0 buy: 1.0 \n" +
                "\n" +
                "\n" +
                "Sberbank\n" +
                "EUR: sell: 70.5 buy: 78.0 \n" +
                "USD: sell: 60.5 buy: 68.0 \n" +
                "RUB: sell: 1.0 buy: 1.0 \n\n\n", arrayListOfAnswers2.get(2).getText());
        assertEquals("Choose Bank", testUser.getStep());
        String message3 = "/back";
        telegramBot.ourScenario(testUser, message3);
        assertEquals("Choose City", testUser.getStep());
        telegramBot.ourScenario(testUser, message2);
        String message4 = "Sberbank";
        ArrayList<SendMessage> arrayListOfAnswers4 =telegramBot.ourScenario(testUser, message4);
        assertEquals("Выберите, Sell или Buy",arrayListOfAnswers4.get(0).getText());
        assertEquals("Sell or Buy", testUser.getStep());
        telegramBot.ourScenario(testUser, "Sell");
        assertEquals("Calculate or Change", testUser.getStep());
    }
}