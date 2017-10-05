package ru.apr.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.apr.MallratbotApplication;
import ru.apr.util.BeanFileLoader;

@Component
public class MallRatBot extends TelegramLongPollingBot {

    @Autowired
    ApplicationContext context;

    @Value(value="${bot.name}")
    protected String botName;

    @Value(value="${bot.token}")
    protected String botToken;

    protected Answer answer;

    @Override
    public void onUpdateReceived(Update update) {
        Message userMessage = update.getMessage();

        String[] command = userMessage.getText().split(" ");

        BeanFileLoader loader = context.getBean(BeanFileLoader.class);
        loader.setBeanName("answer");
        ApplicationContext moduleContext = loader.getModuleContext();

        answer = moduleContext.getBean(Answer.class);
        SendMessage message = new SendMessage();

        if(command.length > 0)
        switch (command[0]){
            case "/start":
                message.setText(answer.getGreetings());
                break;

            // add new shopping list
            case "/add":
                break;

            // add user to shopping list
            case "/permit":
                break;

            // show list subscribers
            case "/sub":
                break;

            // revoke permission
            case "/revoke":
                break;

            // show available lists
            case "/show":
                break;

            // show active list
            case "/list":
                break;

            // put item to the basket
            case "/put":
                break;

            // remove item from the basket (bought or deleted)
            case "/remove":
                break;

            default:
                break;
        }

        message.setChatId(update.getMessage().getChatId());
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
