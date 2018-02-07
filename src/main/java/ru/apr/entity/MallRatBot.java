package ru.apr.entity;

import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.apr.MallratbotApplication;
import ru.apr.entity.dialogue.Answer;
import ru.apr.entity.dialogue.Dialogue;
import ru.apr.messaging.Sendable;
import ru.apr.messagingStrategy.MessagingStrategy;
import ru.apr.messagingStrategy.StaticAnswerStrategy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

@Component
public class MallRatBot extends TelegramLongPollingBot {

    @Autowired
    ApplicationContext context;

    @Value(value="${bot.name}")
    protected String botName;

    @Value(value="${bot.token}")
    protected String botToken;

    protected Answer answer;

    // pair of UserId -> Dialogue Object
    protected static HashMap<Integer, Dialogue> conversations = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        MessagingStrategy messagingStrategy;
        Sendable telegramMessage;

        if(update.getMessage().getText() != null) {
            switch (update.getMessage().getText()){
                case "/start":
                    messagingStrategy = context.getBean(StaticAnswerStrategy.class);
                    telegramMessage = messagingStrategy.messageConstruct(update);
                    break;
                default:
                    messagingStrategy = context.getBean(StaticAnswerStrategy.class);
                    telegramMessage = messagingStrategy.messageConstruct(update);
                    break;
            }

            try {
                sendMessage(telegramMessage.getSendMessageEntity());
            } catch (TelegramApiException e) {
                MallratbotApplication.logger.error(e.getMessage());
            }
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

    public static HashMap<Integer, Dialogue> getConversations() {
        return conversations;
    }

    /**
     * Invokes a callback for a step
     * @param dialogue Dialogue instance
     * @param step step number
     */
    private void invokeCallback(Dialogue dialogue, int step){
        if(dialogue != null && dialogue.getCallbackMap() != null && dialogue.getCallbackMap().containsKey(step)){
            try {
                String callback = dialogue.getCallbackMap().get(step);
                Class callbackClassInstance = null;
                String callbackClassInstanceString = "";
                String methodName = "";

                String[] splitted = callback.split("\\.");

                if(splitted.length > 1){
                    for(int i = 0; i < splitted.length - 1; i++){
                        if(i != splitted.length-1){
                            callbackClassInstanceString += splitted[i];
                        }

                        if(i != splitted.length-2)
                            callbackClassInstanceString += ".";
                    }

                    try {
                        callbackClassInstance = Class.forName(callbackClassInstanceString);
                    } catch (ClassNotFoundException e) {
                        MallratbotApplication.logger.error("Class not found " + e.getMessage());
                    }

                    methodName = splitted[splitted.length-1];
                }
                else{
                    callbackClassInstance = Dialogue.class;
                    methodName = callback;
                }

                Method classMethod = callbackClassInstance.getMethod(methodName);
                classMethod.invoke(callbackClassInstance.newInstance());
            }
            catch(IllegalArgumentException e){
                MallratbotApplication.logger.error("Malformed classname. " + e.getMessage());
            }
              catch (IllegalAccessException e) {
                MallratbotApplication.logger.error("Illegal access. " + e.getMessage());
            } catch (NoSuchMethodException e) {
                MallratbotApplication.logger.error("No such method. " + e.getMessage());
            } catch (InvocationTargetException e) {
                MallratbotApplication.logger.error(e.getMessage());
            } catch (InstantiationException e) {
                MallratbotApplication.logger.error("Can't create an instance" + e.getMessage());
            }
        }
    }
}
