package ru.apr.messagingStrategy;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import ru.apr.messaging.Sendable;

public interface MessagingStrategy {
    Sendable messageConstruct(Update messageData);
    String getMessageContextName();
}
