package com.example.itp_app.utilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.function.Consumer;

public class Requester {
    private HttpClient client;

    public Requester() {
        this.client = HttpClient.newHttpClient();
    }

    public void sendGetRequest(String uri, Consumer<HttpResponse<String>> responseHandler) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        sendRequest(request, responseHandler);
    }

    public void sendPostRequest(String uri, String jsonBody, Consumer<HttpResponse<String>> responseHandler) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        sendRequest(request, responseHandler);
    }
    public void sendPutRequest(String uri, String jsonBody, Consumer<HttpResponse<String>> responseHandler) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        sendRequest(request, responseHandler);
    }

    public void sendDeleteRequest(String uri,Consumer<HttpResponse<String>> responseHandler) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .DELETE()
                .build();
        sendRequest(request, responseHandler);
    }

    private void sendRequest(HttpRequest request, Consumer<HttpResponse<String>> responseHandler) {
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(responseHandler)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }
}