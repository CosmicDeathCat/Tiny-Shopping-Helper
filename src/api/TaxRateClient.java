package api;
import com.google.gson.Gson;
import data.TaxRateResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class TaxRateClient {

    private static final String API_URL = "https://retrieveustaxrate.p.rapidapi.com/GetTaxRateByZip?zip=";
    private static final String AUTH_HEADER = "Basic Ym9sZGNoYXQ6TGZYfm0zY2d1QzkuKz9SLw==";
    private static final String API_KEY = "77600ecdedmsh1931c459c3b1795p1025eejsn9fa34f562588";
    private static final String API_HOST = "retrieveustaxrate.p.rapidapi.com";
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
