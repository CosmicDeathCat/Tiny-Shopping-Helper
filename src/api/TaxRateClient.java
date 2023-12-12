package api;
import com.google.gson.Gson;
import core.Configuration;
import utility.JSONHelper;
import data.TaxRateResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * This class contains methods for the Tax Rate API
 */
public class TaxRateClient {

    private static final String API_URL = Configuration.getApiUrl();
    private static final String API_KEY = Configuration.getApiKey();
    private static final String API_HOST = Configuration.getApiHost();

    /**
     * This is a constructor for the Tax Rate Client
     */
    public TaxRateClient() {

    }

    /**
     * This method builds the URI for the API
     * @param zipCode
     * @param city
     * @param state
     * @return
     */
    private URI buildURI(String zipCode, String city, String state) {
        // Build URI
        StringBuilder uriBuilder = new StringBuilder(API_URL);

        boolean firstParam = true;
        //this if statement checks if zipCode is not null and not empty, and if it is, it adds it to the URI
        if (zipCode != null && !zipCode.isEmpty()) {
            uriBuilder.append(firstParam ? "?" : "&").append("zip_code=").append(zipCode);
            firstParam = false;
        }
        //this if statement checks if city is not null and not empty, and if it is, it adds it to the URI
        if (city != null && !city.isEmpty()) {
            uriBuilder.append(firstParam ? "?" : "&").append("city=").append(city);
            firstParam = false;
        }
        //this if statement checks if state is not null and not empty, and if it is, it adds it to the URI
        if (state != null && !state.isEmpty()) {
            uriBuilder.append(firstParam ? "?" : "&").append("state=").append(state);
        }

        return URI.create(uriBuilder.toString());
    }

    /**
     * This method gets the tax rate from the API
     * @param zipCode
     * @param city
     * @param state
     * @return
     */
    public TaxRateResponse getTaxRateFromZipCityState(String zipCode, String city, String state) {
        try {
            // Creates a HTTP client
            HttpClient client = HttpClient.newHttpClient();
            URI uri = buildURI(zipCode, city, state);
            // Builds a request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("X-RapidAPI-Key", API_KEY)
                    .header("X-RapidAPI-Host", API_HOST)
                    .build();
            // Sends a request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Parses the response
            List<TaxRateResponse> taxRates = JSONHelper.fromJSONList(response.body(), TaxRateResponse.class);
            return taxRates.isEmpty() ? null : taxRates.get(0);
        } catch (Exception e) {
            //this displays an error message if there is an error getting the tax rate
            e.printStackTrace();
            return null;
        }
    }


}
