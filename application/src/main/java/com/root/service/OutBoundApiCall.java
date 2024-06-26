package com.root.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OutBoundApiCall {

    public static String makeApiCall() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Build the HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://worldtimeapi.org/api/timezone/Asia/Kolkata"))
                .GET()  // Default method is GET, can be omitted
                .build();

        // Send the HttpRequest and get the HttpResponse
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
