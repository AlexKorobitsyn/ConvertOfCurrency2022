package BestCurrencyExchangerBot.service;

import Parsers.YandexParser;
import Parsers.BanksParser;
import BestCurrencyExchangerBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    final String[] alphabet = {"Евро", "Доллар", "Рубль"};
    String town = "";
    Set<String> banks = null;
    HashMap<String, HashMap<String, HashMap<String, Double>>> banks_with_currencies;
    String mainBank = "";
    int step = 1;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            YandexParser yaParser = new YandexParser();
            BanksParser bankParser = new BanksParser(alphabet);

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (step == 1) {
                switch (messageText) {
                    case "/start":
                        String text = "Введите город, в котором хотите найти обменник валют";
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        sendMessageWithKeyboard(chatId, text, secondStepKeyboard());
                        step = 2;
                        break;
                    case "/back":
                        sendMessage(chatId, "Некуда отспупать, Вы уже на шаге номер 1");
                        break;
                    default:
                        sendMessage(chatId, "Команда не распознана");
                        break;
                }
            }
            else if (step == 2) {
                switch (messageText) {
                    case "/back":
                        step = 1;
                        sendMessageWithKeyboard(chatId,
                                "Возвращаюсь на шаг номер 1. (Введите /start)", firstStepKeyboard());
                        break;
                    default:
                        sendMessage(chatId, "Запускаю поиск банков в выбранном городе, ожидайте...");
                        town = messageText;
                        yaParser.setText("Город" + town + "Банки");
                        banks = yaParser.ParseTheYandexName(false);

                        banks_with_currencies = bankParser.banks_with_their_exchange_rates(banks);

                        sendMessage(chatId, "Список найденных банков с их курсами:");
                        sendMessage(chatId, BanksParser.make_str_for_output(banks_with_currencies));
                        sendMessageWithKeyboard(chatId, "Выберите Банк", thirdStepKeyboard(banks));
                        step = 3;
                        break;
                }
            }
            else if (step == 3) {
                mainBank = "";
                switch (messageText) {
                    case "/back":
                        step = 2;
                        sendMessageWithKeyboard(chatId,
                                "Возвращаюсь на шаг номер  2. (Выбор города)", secondStepKeyboard());
                        break;
                    default:
                        for (String bank : banks) {
                            if (messageText.equals(bank)) {
                                mainBank = bank;
                            }
                        }
                        sendMessageWithKeyboard(chatId, "Выберите, Calculate или Change", fourthStepKeyboard());
                        step = 4;
                        break;
                    }
                }
            else if (step == 4) {
                switch (messageText) {
                    case "/back":
                        step = 3;
                        sendMessageWithKeyboard(chatId, "Возвращаюсь на шаг номер 3. (Выбор банка)", thirdStepKeyboard(banks));
                        break;
                    case "Calculate":
                        sendMessageWithKeyboard(chatId, "Вы в режиме Калькулятора", fifthStepKeyboard());
                        step = 5;
                        break;
                    case "Change":
                        sendMessageWithKeyboard(chatId, "Вы в режиме Обмена", sixthStepKeyboard());
                        step = 6;
                        break;
                    case "Map":
                        double latitude = 0;
                        double longitude = 0;
                        yaParser.setText("Город" + town + mainBank);
                        Set<String> coordinates = yaParser.ParseTheYandexName(true);
                        for (String coordinate : coordinates) {
                            String[] latAndLong = coordinate.split(",");
                            latitude = Double.valueOf(latAndLong[0]);
                            longitude = Double.valueOf(latAndLong[1]);
                            break;
                        }
                        sendLocation(chatId, longitude, latitude );
                        break;
                    default:
                        sendMessage(chatId, "Команда не распознана");
                        break;
                }
            }
            else if (step == 5) {
                switch (messageText) {
                    case "/back":
                        step = 4;
                        sendMessageWithKeyboard(chatId,
                                "Возращаюсь на шаг номер 4.", fourthStepKeyboard());
                        break;
                }
            }
            else if (step == 6) {
                switch (messageText) {
                    case "/back":
                        step = 4;
                        sendMessageWithKeyboard(chatId,
                                "Возращаюсь на шаг номер 4.", fourthStepKeyboard());
                        break;
                }
            }
        }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<KeyboardRow> firstStepKeyboard() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/start");
        keyboardRows.add(row);
        return keyboardRows;
    }
    private ArrayList<KeyboardRow> secondStepKeyboard() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Екатеринбург");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("/back");
        keyboardRows.add(row);
        return keyboardRows;
    }
    private ArrayList<KeyboardRow> thirdStepKeyboard(Set<String> banks) {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (String bank : banks) {
            row.add(bank);
            keyboardRows.add(row);
            row = new KeyboardRow();
        }
        row.add("/back");
        keyboardRows.add(row);
        return keyboardRows;
    }
    private ArrayList<KeyboardRow> fourthStepKeyboard() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Calculate");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Change");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Map");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("/back");
        keyboardRows.add(row);
        return keyboardRows;
    }

    private ArrayList<KeyboardRow> fifthStepKeyboard() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/back");
        keyboardRows.add(row);
        return keyboardRows;
    }

    private ArrayList<KeyboardRow> sixthStepKeyboard() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/back");
        keyboardRows.add(row);
        return keyboardRows;
    }

    private void startCommandReceived(long chatId, String name){

        String answer = "Привет, " + name +", рады тебя видеть!\n";
        log.info("Replied to user: " + name);
        sendMessage(chatId, answer);
    }

    private void sendMessageWithKeyboard(long chatId, String text, ArrayList<KeyboardRow> keyboardRows){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
        try{
            execute(message);
        }
        catch (TelegramApiException e) {
            log.error("Error occurred:" + e.getMessage());
        }
    }


    private void sendMessage(long chatId, String textToSend)  {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }
        catch (TelegramApiException e) {
            log.error("Error occurred:" + e.getMessage());
        }
    }
    private void sendLocation(long chatId, double latitude, double longitude)  {
        SendLocation location = new SendLocation(String.valueOf(chatId), latitude, longitude);
        try{
            execute(location);
        }
        catch (TelegramApiException e) {
            log.error("Error occurred:" + e.getMessage());
        }
    }
}