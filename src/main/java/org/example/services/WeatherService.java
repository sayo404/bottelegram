package org.example.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService {

    private final String apiKey;
    private final OkHttpClient httpClient;

    public WeatherService(String apiKey, OkHttpClient httpClient) {
        this.apiKey = apiKey;
        this.httpClient = httpClient;
    }

    public String getWeather(String ciudad){
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + ciudad + "&appid=" + apiKey
                + "&units=metric&lang=es";

        Request request = new Request.Builder().url(url).build();

        try(Response response = httpClient.newCall(request).execute()){
            if(!response.isSuccessful() || response.body() == null){
                return "No se encontró la ciudad: " + ciudad;
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
        } catch(Exception e){
            return "Error al consultar el clima.";
        }
    }
}
