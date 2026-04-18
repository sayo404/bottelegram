package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Main implements LongPollingSingleThreadUpdateConsumer {

    private static final String TOKEN = "8767300338:AAFav8NkElE_gpcrf_eih6Z2jIbGGSM-ugs";
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
                            .text("¡Hola! Soy tu bot en Java 🤖☕")
                            .build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}