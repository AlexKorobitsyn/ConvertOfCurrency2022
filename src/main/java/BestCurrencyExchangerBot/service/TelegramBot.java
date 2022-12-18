package BestCurrencyExchangerBot.service;
import CalculateClasses.Interface;
import Parsers.BanksParser;
import BestCurrencyExchangerBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    private ITownStructureFactory townStructureFactory;

    private IBankParserFactory parserFactory;

    private final Interface in = new Interface();
    private ConcurrentHashMap<Long, User> arrayOfUsers = new ConcurrentHashMap<>();

    public TelegramBot(BotConfig config) throws IOException {
        this.config = config;
    }
    public void setITownStructureFactory (ITownStructureFactory factory){
        this.townStructureFactory = factory;
    }
    public void setParserFactory(IBankParserFactory parserFactory) {
        this.parserFactory = parserFactory;
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
        User user;
        townStructureFactory = new TownStructure();
        try {
            Message message = update.getMessage();
            if(update.hasMessage() && message.hasLocation())
            {
                long chatId = message.getChatId();
                if (!arrayOfUsers.containsKey(chatId)) {
                    user = new User();
                    arrayOfUsers.put(chatId, user);
                } else {
                    user = arrayOfUsers.get(chatId);
                }
                user.setFirstName(update.getMessage().getChat().getFirstName());
                user.setChatId(chatId);
                user.setCoordinatesOfUser(message.getLocation());
                user.setStep("Choose City");
                ArrayList<SendMessage> messages = ourScenario(user, user.getTown());
                for (SendMessage messageFromScenario : messages) {
                    try {
                        execute(messageFromScenario);
                    } catch (TelegramApiException e) {
                        log.error("Error occurred:" + e.getMessage());
                    }
                }
            }
            if (update.hasMessage() && message.hasText()) {
                String messageText = message.getText();
                long chatId = message.getChatId();
                if (!arrayOfUsers.containsKey(chatId)) {
                    user = new User();
                    arrayOfUsers.put(chatId, user);
                } else {
                    user = arrayOfUsers.get(chatId);
                }
                user.setFirstName(update.getMessage().getChat().getFirstName());
                user.setChatId(chatId);
                ArrayList<SendMessage> messages = ourScenario(user, messageText);
                for (SendMessage messageFromScenario : messages) {
                    try {
                        execute(messageFromScenario);
                    } catch (TelegramApiException e) {
                        log.error("Error occurred:" + e.getMessage());
                    }
                }
                if (user.isHaveCoordinatesOfBank()) {
                    sendLocation(user.getChatId(), user.getCoordinatesOfBank()[0], user.getCoordinatesOfBank()[1]);
                    user.setHaveCoordinatesOfBank(false);
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
        KeyboardButton button = new KeyboardButton();
        button.setText("Геолокация");
        button.setRequestLocation(true);
        KeyboardRow row = new KeyboardRow();
        row.add(button);
        keyboardRows.add(row);
        button = new KeyboardButton();
        button.setText("/back");
        row = new KeyboardRow();
        row.add(button);
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

    private SendMessage startCommandReceived(long chatId, String name){

        String answer = "Привет, " + name +", рады тебя видеть!\n";
        log.info("Replied to user: " + name);
        return sendMessage(chatId, answer);
    }

    private SendMessage sendMessageWithKeyboard(long chatId, String text, ArrayList<KeyboardRow> keyboardRows){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }
    private void sendMessageWithKeyboardForCoordinate(long chatId, String text, ArrayList<KeyboardRow> keyboardRows){
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

    private SendMessage sendMessage(long chatId, String textToSend)  {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        return message;
    }
    private void sendMessageForCoordinate(long chatId, String textToSend)  {
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

    private double[] mapGetLocation(User user) throws IOException, ParseException {
        double[] result = new double[2];
        double latitude = 0;
        double longitude = 0;
        if (user.isHaveCoordinates()) {
            double userLatitude = user.getCoordinatesOfUser().getLatitude();
            double userLongitude = user.getCoordinatesOfUser().getLongitude();
            double saveLatitude = 0;
            double saveLongitude = 0;
            double minDistance = 1111110111;
            Set<String> coordinates = townStructureFactory.getCoordinates(user.getMainBank(), user.getTown());
            for (String coordinate : coordinates) {
                String[] latAndLong = coordinate.split(",");
                latitude = Double.parseDouble(latAndLong[1]);
                longitude = Double.parseDouble(latAndLong[0]);
                double x = Math.abs(longitude - userLongitude);
                double y = Math.abs(latitude - userLatitude);
                double newDistance = x + y;
                if (newDistance < minDistance) {
                    minDistance = newDistance;
                    saveLatitude = latitude;
                    saveLongitude = longitude;
                }
            }
            result[0] = saveLatitude;
            result[1] = saveLongitude;
        }
        else {
            Set<String> coordinates = townStructureFactory.getCoordinates(user.getMainBank(), user.getTown());
            for (String coordinate : coordinates) {
                String[] latAndLong = coordinate.split(",");
                latitude = Double.parseDouble(latAndLong[1]);
                longitude = Double.parseDouble(latAndLong[0]);
                break;
            }
            result[0] = latitude;
            result[1] = longitude;
        }
        return result;

    }
    public ArrayList<SendMessage> ourScenario(User user, String messageText) throws Exception {
        ArrayList<SendMessage> resultMessage = new ArrayList<SendMessage>();
        if (Objects.equals(user.getStep(), "Start")) {
            switch (messageText) {
                case "/start":
                    String text = "Введите город, в котором хотите найти обменник валют или выберите геолокацию";
                    resultMessage.add(startCommandReceived(user.getChatId(), user.getFirstName()));
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(), text, secondStepKeyboard()));
                    user.setStep("Choose City");
                    break;
                case "/back":
                    resultMessage.add(sendMessage(user.getChatId(), "Некуда отспупать, Вы уже на шаге номер 1"));
                    break;
                default:
                    resultMessage.add(sendMessage(user.getChatId(), "Команда не распознана"));
                    break;
            }
        } else if (Objects.equals(user.getStep(), "Choose City")) {
            switch (messageText) {
                case "/back":
                    user.setStep("Start");
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                            "Возвращаюсь на шаг назад. (Введите /start)", firstStepKeyboard()));
                    break;
                default:
                    resultMessage.add(sendMessage(user.getChatId(), "Запускаю поиск банков в выбранном городе, ожидайте..."));
                    user.setTown(messageText);
                    user.setTownStructure(townStructureFactory.buildTownStructure(user.getTown()));

                    resultMessage.add(sendMessage(user.getChatId(), "Список найденных банков с их курсами:"));
                    resultMessage.add(sendMessage(user.getChatId(), BanksParser.makeOutputStr(user.getTownStructure().getBanksWithCurrencies())));
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(), "Выберите Банк", thirdStepKeyboard(user.getTownStructure().getBanks())));
                    user.setStep("Choose Bank");
                    break;
            }
        } else if (Objects.equals(user.getStep(), "Choose Bank")) {
            switch (messageText) {
                case "/back":
                    user.setStep("Choose City");
                    user.setHaveCoordinates(false);
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                            "Возвращаюсь на шаг назад. (Выбор города)", secondStepKeyboard()));
                    break;
                default:
                    for (String bank : user.getTownStructure().getBanks()) {
                        if (messageText.equals(bank)) {
                            user.setMainBank(bank);
                        }
                    }
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(), "Выберите, Sell или Buy", fifthStepKeyboard()));
                    user.setStep("Sell or Buy");
                    break;
            }
        } else if (Objects.equals(user.getStep(), "Sell or Buy")) {
            switch (messageText) {
                case "Sell":
                    user.setBankCurrenciesForCalculate(BanksParser.makeDictForCalculate(user.getTownStructure().getBanksWithCurrencies().get(user.getMainBank()), "sell"));
                    user.setStep("Calculate or Change");
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(), "Выберите, Calculate или Change", fourthStepKeyboard()));
                    break;
                case "Buy":
                    user.setBankCurrenciesForCalculate(BanksParser.makeDictForCalculate(user.getTownStructure().getBanksWithCurrencies().get(user.getMainBank()), "buy"));
                    user.setStep("Calculate or Change");
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(), "Выберите, Calculate или Change", fourthStepKeyboard()));
                    break;
                case "/back":
                    user.setStep("Choose Bank");
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                            "Возвращаюсь на шаг назад", thirdStepKeyboard(user.getTownStructure().getBanks())));
                    break;
                default:
                    resultMessage.add(sendMessage(user.getChatId(), "Команда не распознана"));
                    break;
            }
        } else if (Objects.equals(user.getStep(), "Calculate or Change")) {
            switch (messageText) {
                case "/back":
                    user.setStep("Sell or Buy");
                    sendMessageWithKeyboardForCoordinate(user.getChatId(), "Возвращаюсь на шаг назад.", fifthStepKeyboard());
                    break;
                case "Calculate":
                    sendMessageWithKeyboardForCoordinate(user.getChatId(),
                            "Введите выражение", seventhStepKeyboard());
                    user.setStep("Expression for Calculate");
                    break;
                case "Change":
                    sendMessageWithKeyboardForCoordinate(user.getChatId(), "Вы в режиме Обмена", sixthStepKeyboard());
                    sendMessageForCoordinate(user.getChatId(), "Введите валюту котороую хотите перевести");
                    user.setStep("Expression for Change Part1");
                    break;
                case "Map":
                    double[] location = mapGetLocation(user);
                    user.setCoordinatesOfBank(location);
                    user.setHaveCoordinatesOfBank(true);
                    break;
                default:
                    sendMessageForCoordinate(user.getChatId(), "Команда не распознана");
                    break;
            }
        }else if (Objects.equals(user.getStep(), "Expression for Calculate")) {
            switch (messageText) {
                case "/help":
                    resultMessage.add(sendMessage(user.getChatId(), """
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
                            OUT: 1192,488 RUB"""));
                    break;
                case "/back":
                    user.setStep("Calculate or Change");
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                            "Возвращаюсь на шаг назад.", fourthStepKeyboard()));
                    break;
                default:
                    resultMessage.add(sendMessage(user.getChatId(), in.input(messageText, user.getBankCurrenciesForCalculate())));
                    break;
            }
        } else if (Objects.equals(user.getStep(), "Expression for Change Part1")) {
            switch (messageText) {
                case "EUR":
                case "RUB":
                case "USD":
                    user.setFirstCurrency(messageText);
                    user.setStep("Expression for Change Part2");
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                            "Введите кол-во единиц данной валюты", eighthStepKeyboard()));
                    break;
                case "/back":
                    user.setStep("Calculate or Change");
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                            "Возращаюсь на шаг назад", fourthStepKeyboard()));
                    break;
                default:
                    resultMessage.add(sendMessage(user.getChatId(), "Команда не распознана"));
                    break;
            }
        } else if (Objects.equals(user.getStep(), "Expression for Change Part2")) {
            switch (messageText) {
                case "/back":
                    user.setStep("Expression for Change Part1");
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                            "Выберите валюту которую хотите обменять", sixthStepKeyboard()));
                    break;
                default:
                    try {
                        user.setNumberOfFirstCurrency(Integer.parseInt(messageText));
                        user.setStep("Expression for Change Part3");
                        resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                                "Выберите валюту в которую переводим", ninthStepKeyboard()));
                    } catch (NumberFormatException e) {
                        resultMessage.add(sendMessage(user.getChatId(), "Неправильный формат ввода\nВведите натуральное число"));
                    }
                    break;
            }
        } else if (Objects.equals(user.getStep(), "Expression for Change Part3")) {
            switch (messageText) {
                case "USD":
                case "EUR":
                case "RUB":
                    user.setSecondCurrency(messageText);
                    user.setStep("Expression for Change Part1");
                    String expr = user.getNumberOfFirstCurrency() + " " + user.getFirstCurrency() + " in " + user.getSecondCurrency();
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                            in.input(expr, user.getBankCurrenciesForCalculate()) +
                                    "\nПовторим? Выберите валюту которую хотите обменять", sixthStepKeyboard()));
                    break;
                case "/back":
                    user.setStep("Expression for Change Part2");
                    resultMessage.add(sendMessageWithKeyboard(user.getChatId(),
                            "Введите кол-во единиц данной валюты", eighthStepKeyboard()));
                default:
                    resultMessage.add(sendMessage(user.getChatId(), "Команда не распознана"));
                    break;
            }
        }
        return resultMessage;
    }
}