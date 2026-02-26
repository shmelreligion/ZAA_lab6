package api;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class DndApiClient {
    private static final String BASE = "https://www.dnd5eapi.co/api";

    public String getString(String endpoint) {
        HttpResponse<String> response = Unirest.get(BASE + endpoint).asString();
        if (response.getStatus() != 200) {
            throw new RuntimeException("HTTP " + response.getStatus() + " для " + endpoint);
        }
        return response.getBody();
    }
}
