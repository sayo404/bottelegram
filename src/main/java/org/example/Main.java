package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.bot.TelegramBot;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {

    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String token = dotenv.get("TELEGRAM_TOKEN");
        String weatherApiKey = dotenv.get("OPENWEATHER_API_KEY");
        String exchangeApiKey = dotenv.get("EXCHANGE_API_KEY");

        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
        app.registerBot(token, new TelegramBot(token, weatherApiKey, exchangeApiKey));
        System.out.println("¡Bot iniciado!");
        Thread.currentThread().join();
    }
}