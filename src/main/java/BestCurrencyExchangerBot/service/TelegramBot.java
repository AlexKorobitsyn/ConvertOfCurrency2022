package BestCurrencyExchangerBot.service;

import BestCurrencyExchangerBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    int step = 1;
    final BotConfig config;

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

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (step == 1) {
                switch (messageText) {
                    case "/start":
//                        sendKeyboard(chatId, secondStepKeyboard());
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        step = 2;
                        break;
                    case "/back":
                        sendMessage(chatId, "Некуда отспупать, Вы уже на шаге номер 1");
                        break;
                    case "Mitya":
                        sendMessage(chatId, "Что делаешь " + update.getMessage().getChat().getFirstName()+"?");
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
                        sendMessage(chatId, "Возвращаюсь на шаг номер 1. (Введите /start)");
                        break;
                    default:
                        sendMessage(chatId, "Запускаю поиск банков в выбранном городе");
                        sendMessage(chatId, "Список найденных банков с их курсами:");
                        sendMessage(chatId, "Выберите Банк");
                        step = 3;
                        break;
                }
            }
            else if (step == 3) {
                switch (messageText) {
                    case "/back":
                        step = 2;
                        sendMessage(chatId, "Возвращаюсь на шаг номер  2. (Выбор города)");
                        break;
                    default:
                        sendLocation(chatId, 51.111, 41.1111 );
                        sendMessage(chatId, "Выберите, Calculate или Change");
                        break;
                }
            }
            else if (step == 4) {
                switch (messageText) {
                    case "/back":
                        step = 3;
                        sendMessage(chatId, "Возвращаюсь на шаг номер 3. (Выбор банка)");
                        break;
                    case "Calculate":
                        sendMessage(chatId, "Вы в режиме Калькулятора");
                        step = 4;
                        break;
                    case "Change":
                        sendMessage(chatId, "Вы в режиме Обмена");
                        step = 4;
                        break;
                    default:
                        sendMessage(chatId, "Команда не распознана");
                        break;
                }
            }
        }
    }

//    private ArrayList<KeyboardRow> secondStepKeyboard() {
//        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
//        KeyboardRow row = new KeyboardRow();
//        row.add("Екатеринбург");
//        keyboardRows.add(row);
//        row = new KeyboardRow();
//        row.add("/back");
//        keyboardRows.add(row);
//        return keyboardRows;
//    }

    private void startCommandReceived(long chatId, String name){

        String answer = "Привет, " + name +", рады тебя видеть!" +
                "\nВведите город, в котором хотите найти обменник валют";
        log.info("Replied to user: " + name);
        sendMessage(chatId, answer);
//        sendLocation(chatId,51.111, 41.1111 );
    }

//    private void sendKeyboard(long chatId, ArrayList<KeyboardRow> keyboardRows){
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText("");
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        keyboardMarkup.setKeyboard(keyboardRows);
//        message.setReplyMarkup(keyboardMarkup);
//        try{
//            execute(message);
//        }
//        catch (TelegramApiException e) {
//            log.error("Error occurred:" + e.getMessage());
//        }
//    }


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