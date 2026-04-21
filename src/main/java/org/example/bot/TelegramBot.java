package org.example.bot;

import okhttp3.OkHttpClient;
import org.example.services.ExchangeService;
import org.example.services.NewsService;
import org.example.services.WeatherService;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient client;
    private final WeatherService weatherService;
    private final ExchangeService exchangeService;
    private final NewsService newsService;

    public TelegramBot(String token, String weatherApiKey, String exchangeApiKey, String newsApiKey) {
        OkHttpClient httpClient = new OkHttpClient();
        this.client = new OkHttpTelegramClient(token);
        this.weatherService = new WeatherService(weatherApiKey, httpClient);
        this.exchangeService = new ExchangeService(exchangeApiKey, httpClient);
        this.newsService = new NewsService(newsApiKey, httpClient);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String texto = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String respuesta;

            if (texto.equals("/start")) {
                respuesta = "¡Hola! Aquí tienes mis comandos:\n/clima <ciudad> — Consulta el clima\n/dolar — Tipo de cambio\n/noticias — Últimas noticias";
            } else if (texto.startsWith("/clima ")) {
                String ciudad = texto.substring(7);
                respuesta = weatherService.getWeather(ciudad);
            } else if (texto.equals("/dolar")) {
                respuesta = exchangeService.getExchangeRate();
            } else if (texto.equals("/noticias")) {
                respuesta = newsService.getNews();
            } else {
                respuesta = "No entiendo ese comando.\n/clima <ciudad> — Consulta el clima\n/dolar — Tipo de cambio";
            }

            try {
                client.execute(SendMessage.builder()
                        .chatId(chatId)
                        .text(respuesta)
                        .build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}