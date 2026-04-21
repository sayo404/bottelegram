package org.example.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NewsService {

    private final String apiKey;
    private final OkHttpClient httpClient;

    public NewsService(String apiKey, OkHttpClient httpClient) {
        this.apiKey = apiKey;
        this.httpClient = httpClient;
    }

    public String getNews(){
        String url = "https://newsapi.org/v2/everything?q=mexico"
                + "&language=es&sortBy=publishedAt&pageSize=5&apiKey=" + apiKey;

        Request request = new Request.Builder().url(url).build();

        try(Response response = httpClient.newCall(request).execute()){
            if(!response.isSuccessful() || response.body() == null){
                return "Error al consultar las noticias";
            }

            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            JsonArray articles = json.getAsJsonArray("articles");

            if(articles.size() == 0){
                return "No encontré noticias en este momento";
            }

            StringBuilder sb = new StringBuilder("Ultimas noticias:\n\n");

            for(int i=0; i < articles.size(); i++){
                JsonObject article = articles.get(i).getAsJsonObject();
                String titulo = article.get("title").getAsString();
                String fuente = article.getAsJsonObject("source").get("name").getAsString();
                sb.append(String.format("%d. %s\n📌 %s\n\n", i + 1, titulo, fuente));
            }

            return sb.toString();
        }catch (Exception e){
            return "Error al consultar las noticias";
        }
    }
}
