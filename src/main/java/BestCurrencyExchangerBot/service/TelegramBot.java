package BestCurrencyExchangerBot.service;
import Calculate_Classes.Interface;
import Parsers.YandexParser;
import Parsers.BanksParser;
import BestCurrencyExchangerBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    final String[] alphabet = {"Евро", "Доллар", "Рубль"};

    HashMap<String, Double> bank_currencies_for_calculate = new HashMap<>();
    Interface in = new Interface();
    String town = "";
    Set<String> banks = null;
    HashMap<String, HashMap<String, HashMap<String, Double>>> banks_with_currencies;
    String mainBank = "";
    String step = "Start";

    String first_currency = "";

    Integer number_of_first_currency;

    String second_currency = "";

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

            if (Objects.equals(step, "Start")) {
                switch (messageText) {
                    case "/start":
                        String text = "Введите город, в котором хотите найти обменник валют";
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        sendMessageWithKeyboard(chatId, text, secondStepKeyboard());
                        step = "Choose City";
                        break;
                    case "/back":
                        sendMessage(chatId, "Некуда отспупать, Вы уже на шаге номер 1");
                        break;
                    default:
                        sendMessage(chatId, "Команда не распознана");
                        break;
                }
            }
            else if (Objects.equals(step, "Choose City")) {
                switch (messageText) {
                    case "/back":
                        step = "Start";
                        sendMessageWithKeyboard(chatId,
                                "Возвращаюсь на шаг назад. (Введите /start)", firstStepKeyboard());
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
                        step = "Choose Bank";
                        break;
                }
            }
            else if (Objects.equals(step, "Choose Bank")) {
                mainBank = "";
                switch (messageText) {
                    case "/back":
                        step = "Choose City";
                        sendMessageWithKeyboard(chatId,
                                "Возвращаюсь на шаг назад. (Выбор города)", secondStepKeyboard());
                        break;
                    default:
                        for (String bank : banks) {
                            if (messageText.equals(bank)) {
                                mainBank = bank;
                            }
                        }
                        sendMessageWithKeyboard(chatId, "Выберите, Sell или Buy", fifthStepKeyboard());
                        step = "Sell or Buy";
                        break;
                    }
                }
            else if (Objects.equals(step, "Calculate or Change")) {
                switch (messageText) {
                    case "/back":
                        step = "Choose Bank";
                        sendMessageWithKeyboard(chatId, "Возвращаюсь на шаг назад.", fifthStepKeyboard());
                        break;
                    case "Calculate":
                        sendMessageWithKeyboard(chatId,
                                "Введите выражение", seventhStepKeyboard());
                        step = "Expression for Calculate";
                        break;
                    case "Change":
                        sendMessageWithKeyboard(chatId, "Вы в режиме Обмена", sixthStepKeyboard());
                        sendMessage(chatId, "Введите валюту котороую хотите перевести");
                        step = "Expression for Change Part1";
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
            else if (Objects.equals(step, "Sell or Buy")) {
                switch (messageText) {
                    case "Sell":
                        bank_currencies_for_calculate =
                                BanksParser.make_dict_for_calculate(banks_with_currencies.get(mainBank), "sell");
                        step = "Calculate or Change";
                        sendMessageWithKeyboard(chatId, "Выберите, Calculate или Change", fourthStepKeyboard());
                        break;
                    case "Buy":
                        bank_currencies_for_calculate =
                                BanksParser.make_dict_for_calculate(banks_with_currencies.get(mainBank), "buy");
                        step = "Calculate or Change";
                        sendMessageWithKeyboard(chatId, "Выберите, Calculate или Change", fourthStepKeyboard());
                        break;
                    case "/back":
                        step = "Choose Bank";
                        sendMessageWithKeyboard(chatId,
                                "Возвращаюсь на шаг назад", thirdStepKeyboard(banks));
                        break;
                    default:
                        sendMessage(chatId, "Команда не распознана");
                        break;
                }
            }
            else if (Objects.equals(step, "Expression for Calculate")) {
                switch (messageText) {
                    case "/help":
                        sendMessage(chatId, """
                                Currency conversion. The simplest calculations in different currencies

                                Arguments:
                                QuantityOfCurrency = QOC
                                NameOfCurrency = NOC
                                Answer - Double variable
                                Domains of definition
                                -1.7 * 10^308 <= QOC <= 1.7 * 10^308
                                -1.7 * 10^308 <= Answer <= 1.7 * 10^308\t
                                NOC: USD, RUB, EUR
                                Examples:
                                IN: QOC NOC + QOC NOC + ... + QOC NOC IN NOC
                                OUT: Answer
                                \t
                                IN: 10 USD + 12 EUR in RUB
                                OUT: 1192,488 RUB""");
                        break;
                    case "/back":
                        step = "Calculate or Change";
                        sendMessageWithKeyboard(chatId,
                                "Возвращаюсь на шаг назад.", fourthStepKeyboard());
                        break;
                    default:
                        sendMessage(chatId, in.input(messageText, bank_currencies_for_calculate));
                        break;
                }
            }
            else if (Objects.equals(step, "Expression for Change Part1")) {
                switch (messageText) {
                    case "EUR":
                    case "RUB":
                    case "USD":
                        first_currency = messageText;
                        step = "Expression for Change Part2";
                        sendMessageWithKeyboard(chatId,
                                "Введите кол-во единиц данной валюты", eighthStepKeyboard());
                        break;
                    case "/back":
                        step = "Calculate or Change";
                        sendMessageWithKeyboard(chatId,
                                "Возращаюсь на шаг назад", fourthStepKeyboard());
                        break;
                    default:
                        sendMessage(chatId, "Команда не распознана");
                        break;
                }
            }
            else if (Objects.equals(step, "Expression for Change Part2")){
                switch (messageText) {
                    case "/back":
                        step = "Expression for Change Part1";
                        sendMessageWithKeyboard(chatId,
                                "Выберите валюту которую хотите обменять", sixthStepKeyboard());
                        break;
                    default:
                        try {
                            number_of_first_currency = Integer.parseInt(messageText);
                            step = "Expression for Change Part3";
                            sendMessageWithKeyboard(chatId,
                                    "Выберите валюту в которую переводим", ninthStepKeyboard());
                        }
                        catch (NumberFormatException e){
                            sendMessage(chatId, "Неправильный формат ввода\nВведите натуральное число");
                        }
                        break;
                }
            }
            else if (Objects.equals(step, "Expression for Change Part3")){
                switch (messageText){
                    case "USD":
                    case "EUR":
                    case "RUB":
                        second_currency = messageText;
                        step = "Expression for Change Part1";
                        String expr = number_of_first_currency + " " + first_currency + " in " + second_currency;
                        sendMessageWithKeyboard(chatId,
                                in.input(expr, bank_currencies_for_calculate) +
                                        "\nПовторим? Выберите валюту которую хотите обменять", sixthStepKeyboard());
                        break;
                    case "/back":
                        step = "Expression for Change Part2";
                        sendMessageWithKeyboard(chatId,
                                "Введите кол-во единиц данной валюты", eighthStepKeyboard());
                    default:
                        sendMessage(chatId, "Команда не распознана");
                        break;
                }
            }
        }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
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
        row.add("Sell");
        row.add("Buy");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("/back");
        keyboardRows.add(row);
        return keyboardRows;
    }

    private ArrayList<KeyboardRow> sixthStepKeyboard() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("EUR");
        row.add("USD");
        row.add("RUB");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("/back");
        keyboardRows.add(row);
        return keyboardRows;
    }

    private ArrayList<KeyboardRow> seventhStepKeyboard() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/help");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("/back");
        keyboardRows.add(row);
        return keyboardRows;
    }
    private ArrayList<KeyboardRow> eighthStepKeyboard() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/back");
        keyboardRows.add(row);
        return keyboardRows;
    }
    private ArrayList<KeyboardRow> ninthStepKeyboard() {
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("EUR");
        row.add("USD");
        row.add("RUB");
        keyboardRows.add(row);
        row = new KeyboardRow();
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