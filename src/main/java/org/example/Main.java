package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

//LEER LAS VARIABLES DE AMBIENTE
import io.github.cdimascio.dotenv.Dotenv;

public class Main implements LongPollingSingleThreadUpdateConsumer {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String TOKEN = dotenv.get("TELEGRAM_TOKEN");
    private final TelegramClient client = new OkHttpTelegramClient(TOKEN);

    public static void main(String[] args) throws Exception {
        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
        app.registerBot(TOKEN, new Main());
        System.out.println("¡Bot iniciado!");
        Thread.currentThread().join();
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String texto = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (texto.equals("/start")) {
                try {
                    client.execute(SendMessage.builder()
                            .chatId(chatId)
                            .text("¡Hola! Soy tu bot en Java :)")
                            .build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}