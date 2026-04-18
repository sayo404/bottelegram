package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

//LEER LAS VARIABLES DE AMBIENTE
import io.github.cdimascio.dotenv.Dotenv;

//PROCESAR JSON DE RESPUESTA
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//PETICIONES HTTP
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main implements LongPollingSingleThreadUpdateConsumer {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String TOKEN = dotenv.get("TELEGRAM_TOKEN");
    private static final String WEATHER_API_KEY = dotenv.get("OPENWEATHER_API_KEY");
    private final TelegramClient client = new OkHttpTelegramClient(TOKEN);
    private final OkHttpClient httpClient = new OkHttpClient();

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
            String respuesta;

            if (texto.equals("/start")) {
                respuesta = "¡Hola! Soy tu bot del clima 🌤️\nUsa /clima <ciudad> para consultar el clima.\nEjemplo: /clima Pachuca";
            } else if (texto.startsWith("/clima ")) {
                String ciudad = texto.substring(7);
                respuesta = obtenerClima(ciudad);
            } else {
                respuesta = "No entiendo ese comando. Usa /clima <ciudad>";
                respuesta = "No entiendo ese comando. Usa /clima <ciudad>";
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

    private String obtenerClima(String ciudad) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + ciudad + "&appid=" + WEATHER_API_KEY
                + "&units=metric&lang=es";

        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return "No encontré la ciudad: " + ciudad;
            }

            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            String nombreCiudad = json.get("name").getAsString();
            String pais = json.getAsJsonObject("sys").get("country").getAsString();
            double temp = json.getAsJsonObject("main").get("temp").getAsDouble();
            double sensacion = json.getAsJsonObject("main").get("feels_like").getAsDouble();
            int humedad = json.getAsJsonObject("main").get("humidity").getAsInt();
            String descripcion = json.getAsJsonArray("weather")
                    .get(0).getAsJsonObject()
                    .get("description").getAsString();

            return String.format(
                    "🌍 %s, %s\n🌡️ Temperatura: %.1f°C\n🤔 Sensación: %.1f°C\n💧 Humedad: %d%%\n☁️ %s",
                    nombreCiudad, pais, temp, sensacion, humedad, descripcion
            );

        } catch (Exception e) {
            return "Error al consultar el clima.";
        }
    }
}