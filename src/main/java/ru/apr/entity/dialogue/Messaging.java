package ru.apr.entity.dialogue;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.apr.MallratbotApplication;

@Component
public class Messaging {
    private Update incomingMessageUpdate;

    public Update getIncomingMessageUpdate() {
        return incomingMessageUpdate;
    }

    public void setIncomingMessageUpdate(Update incomingMessageUpdate) {
        this.incomingMessageUpdate = incomingMessageUpdate;
    }

    public String getCommand(){
        String command = null;
        String messageText = incomingMessageUpdate.getMessage().getText();
        String[] commands = messageText.split(" ");

        if(commands.length > 0){
            command = commands[0];
        }

        return command;
    }

    public Integer getUserId(){
        return incomingMessageUpdate.getMessage().getFrom().getId();
    }

    public SendMessage prepareMessage(String text){
        SendMessage message = new SendMessage();
        message.setText(text);

        message.setChatId(incomingMessageUpdate.getMessage().getChatId());

        return message;
    }
}
