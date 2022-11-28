package BestCurrencyExchangerBot.service;

import BestCurrencyExchangerBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

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


            switch (messageText) {
                case "/start":

                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "Mitya":
                    sendMessage(chatId, "Что делаешь " + update.getMessage().getChat().getFirstName()+"?");
                    break;

                default:
                    sendMessage(chatId, "Команда не распознана");

            }
        }
    }

    private void startCommandReceived(long chatId, String name){

        String answer = "Привет, " + name +", рады тебя видеть!";
        log.info("Replied to user: " + name);
        sendMessage(chatId, answer);
        sendLocation(chatId,51.111, 41.1111 );
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