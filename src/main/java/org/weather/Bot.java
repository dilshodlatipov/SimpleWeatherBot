package org.weather;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Bot extends TelegramLongPollingBot {
    private static final Bot bot = new Bot();

    public static Bot getInstance() {
        return bot;
    }
    @Override
    public String getBotUsername() {
        return "BOT'S USERNAME";
    }

    @Override
    public String getBotToken() {
        return "BOT'S TOKEN";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();
        if (message.hasText()) {
            sendWeather(user.getId(), message.getText());
        }
    }

    private void sendWeather(Long who, String text) {
        String url = "https://wttr.in/" + text + "?format=4";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            SendMessage sm = SendMessage.builder()
                    .parseMode("HTML")
                    .text(response.body())
                    .chatId(who.toString()).build();
            execute(sm);
        } catch (URISyntaxException | IOException | InterruptedException | TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
