package api;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import data.TaxRateResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TaxRateClient {

    private static final String API_URL = "https://sales-tax-by-api-ninjas.p.rapidapi.com/v1/salestax";
    private static final String API_KEY = "6b45072e8cmshabbcd707434ba6ap1d4fbdjsn0350b846cc80";
    private static final String API_HOST = "sales-tax-by-api-ninjas.p.rapidapi.com";
    private final Gson gson;

    public TaxRateClient() {
        gson = new Gson();
    }

    private URI buildURI(String zipCode, String city, String state) {
        StringBuilder uriBuilder = new StringBuilder(API_URL);

        boolean firstParam = true;

        if (zipCode != null && !zipCode.isEmpty()) {
            uriBuilder.append(firstParam ? "?" : "&").append("zip_code=").append(zipCode);
            firstParam = false;
        }
        if (city != null && !city.isEmpty()) {
            uriBuilder.append(firstParam ? "?" : "&").append("city=").append(city);
            firstParam = false;
        }
        if (state != null && !state.isEmpty()) {
            uriBuilder.append(firstParam ? "?" : "&").append("state=").append(state);
        }

        return URI.create(uriBuilder.toString());
    }

    public TaxRateResponse getTaxRateFromZipCityState(String zipCode, String city, String state) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = buildURI(zipCode, city, state);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("X-RapidAPI-Key", API_KEY)
                    .header("X-RapidAPI-Host", API_HOST)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            List<TaxRateResponse> taxRates = gson.fromJson(response.body(), new TypeToken<List<TaxRateResponse>>(){}.getType());
            return taxRates.isEmpty() ? null : taxRates.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
