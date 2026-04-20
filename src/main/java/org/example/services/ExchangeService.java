package org.example.services;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExchangeService {

    private final String apiKey;
    private final OkHttpClient httpClient;

    public ExchangeService(String apiKey, OkHttpClient httpClient) {
        this.apiKey = apiKey;
        this.httpClient = httpClient;
    }

    public String getExchangeRate(){
        String url = "https://openexchangerates.org/api/latest.json?app_id="
                + apiKey + "&symbols=MXN,EUR,CAD";

        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()){
            if (!response.isSuccessful() || response.body() == null) {
                return "❌ Error al consultar el tipo de cambio.";
            }

            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            JsonObject rates = json.getAsJsonObject("rates");

            double mxn = rates.get("MXN").getAsDouble();
            double eur = rates.get("EUR").getAsDouble();
            double cad = rates.get("CAD").getAsDouble();

            return String.format(
                    "💱 Tipo de cambio (base USD)\n\n🇲🇽 MXN: %.4f\n🇪🇺 EUR: %.4f\n🇨🇦 CAD: %.4f",
                    mxn, eur, cad
            );
        }catch (Exception e){
            return "Error al consultar el tipo de cambio";
        }
    }
}
