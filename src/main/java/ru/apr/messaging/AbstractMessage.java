package ru.apr.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

@Component
public class AbstractMessage implements Sendable {
    @Autowired
    ApplicationContext context;

    protected Update incomingData;
    protected String text;

    @Override
    public String getSendableMessageText() {
        return text;
    }

    @Override
    public void setSendableMessageText(String text) {
        this.text = text;
    }

    @Override
    public void setIncomingData(Update incomingMessageUpdateData) {
        incomingData = incomingMessageUpdateData;
    }

    /**
     * Convert Sendable to SendMessage
     * @return SendMessage object
     */
    @Override
    public Update getIncomingData() {
        return incomingData;
    }

    public SendMessage getSendMessageEntity(){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(incomingData.getMessage().getChatId());
        sendMessage.setText(text);

        return sendMessage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
