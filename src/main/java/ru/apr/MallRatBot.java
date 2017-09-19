package ru.apr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Component
public class MallRatBot extends TelegramLongPollingBot {

    @Autowired
    ApplicationContext context;

    @Value(value="${bot.name}")
    protected String botName;

    @Value(value="${bot.token}")
    protected String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(update.getMessage().getChatId())
                .setText(update.getMessage().getText());
        try {
            sendMessage(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
