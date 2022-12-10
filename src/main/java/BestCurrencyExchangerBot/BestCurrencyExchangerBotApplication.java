package BestCurrencyExchangerBot;

import BestCurrencyExchangerBot.service.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ThreadFactory;

@SpringBootApplication
public class BestCurrencyExchangerBotApplication{
    public static void main(String[] args) {

        SpringApplication.run(BestCurrencyExchangerBotApplication.class, args);

    }

}
