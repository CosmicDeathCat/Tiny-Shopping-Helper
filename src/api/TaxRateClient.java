package api;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.Configuration;
import data.TaxRateResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class TaxRateClient {

    private static final String API_URL = Configuration.getApiUrl();
    private static final String API_KEY = Configuration.getApiKey();
    private static final String API_HOST = Configuration.getApiHost();
    private final Gson gson;

    public TaxRateClient() {
        gson = new Gson();
    }

    public TaxRateResponse getTaxRateByZip(String zipCode) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + zipCode))
                    .header("Authorization", AUTH_HEADER)
                    .header("X-RapidAPI-Key", API_KEY)
                    .header("X-RapidAPI-Host", API_HOST)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response using Gson
            return gson.fromJson(response.body(), TaxRateResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
