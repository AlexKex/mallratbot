package ru.apr.messaging;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public interface Sendable {
    String getSendableMessageText();
    void setSendableMessageText(String text);
    SendMessage getSendMessageEntity();
    void setIncomingData(Update incomingMessageUpdateData);
    Update getIncomingData();

}
