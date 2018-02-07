package ru.apr.messagingStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import ru.apr.messaging.Sendable;
import ru.apr.messaging.StaticAnswerMessage;
import ru.apr.util.BeanFileLoader;

@Component
public class StaticAnswerStrategy implements MessagingStrategy {
    @Autowired
    ApplicationContext context;

    /**
     * Construct method for static messages
     * Static message get it's text from XML config file, depending on user command
     * @param incomingMessageUpdateData
     * @return
     */
    @Override
    public Sendable messageConstruct(Update incomingMessageUpdateData) {
        BeanFileLoader loader = context.getBean(BeanFileLoader.class);

        // get command without any non-character signs
        String commandName = incomingMessageUpdateData.getMessage().getText().replaceAll("\\W", "");
        ApplicationContext moduleContext = loader.getModuleContext(commandName);

        StaticAnswerMessage message = moduleContext.getBean(StaticAnswerMessage.class);
        message.setIncomingData(incomingMessageUpdateData);

        return message;
    }

    @Override
    public String getMessageContextName() {
        return null;
    }
}
