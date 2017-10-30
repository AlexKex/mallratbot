package ru.apr.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.apr.MallratbotApplication;
import ru.apr.entity.dialogue.Answer;
import ru.apr.entity.dialogue.Dialogue;
import ru.apr.entity.dialogue.Messaging;
import ru.apr.util.BeanFileLoader;

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
        Messaging msg = context.getBean(Messaging.class);
        msg.setIncomingMessageUpdate(update);

        BeanFileLoader loader = context.getBean(BeanFileLoader.class);

        if(msg.getCommand() != null) {
            switch (msg.getCommand()) {
                case "/start":
                    ApplicationContext moduleContext = loader.getModuleContext("answer");
                    answer = moduleContext.getBean(Answer.class);

                    SendMessage message = msg.prepareMessage(answer.getGreetings());

                    try {
                        sendMessage(message); // Call method to send the message
                    } catch (TelegramApiException e) {
                        MallratbotApplication.logger.error(e.getMessage());
                    }
                    break;

                // add new shopping list
                case "/add":
                    ApplicationContext dialogModuleContext = loader.getModuleContext("dialogue_add");
                    Dialogue dialogueAdd = dialogModuleContext.getBean(Dialogue.class);
                    dialogueAdd.setUserId(msg.getUserId());
                    dialogueAdd.setDialogueType("ADD");

                    if(conversations.containsKey(msg.getUserId())){
                        conversations.remove(msg.getUserId());
                    }

                    int currentStep = dialogueAdd.getCurrentStep();

                    conversations.put(msg.getUserId(), dialogueAdd);
                    SendMessage addMessage = msg.prepareMessage(dialogueAdd.proceedConversation());

                    // invoke a callback in the end of current step
                    if(dialogueAdd.getCallbackMap() != null && dialogueAdd.getCallbackMap().containsKey(currentStep)){
                        try {
                            Method classMethod = Dialogue.class.getMethod(dialogueAdd.getCallbackMap().get(currentStep));
                            classMethod.invoke(dialogueAdd);
                        } catch (IllegalAccessException e) {
                            MallratbotApplication.logger.error(e.getMessage());
                        } catch (NoSuchMethodException e) {
                            MallratbotApplication.logger.error(e.getMessage());
                        } catch (InvocationTargetException e) {
                            MallratbotApplication.logger.error(e.getMessage());
                        }
                    }

                    try {
                        sendMessage(addMessage); // Call method to send the message
                    } catch (TelegramApiException e) {
                        MallratbotApplication.logger.error(e.getMessage());
                    }
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
                    if(conversations.containsKey(msg.getUserId())){
                        Dialogue currentDialogue = conversations.get(msg.getUserId());
                        SendMessage proceedMessage = msg.prepareMessage(currentDialogue.proceedConversation());

                        try {
                            sendMessage(proceedMessage); // Call method to send the message
                        } catch (TelegramApiException e) {
                            MallratbotApplication.logger.error(e.getMessage());
                        }
                        break;
                    }
                    else{
                        ApplicationContext errorContext = loader.getModuleContext("answer");
                        answer = errorContext.getBean(Answer.class);

                        SendMessage errorMessage = msg.prepareMessage(answer.getParseMessageError());

                        try {
                            sendMessage(errorMessage); // Call method to send the message
                        } catch (TelegramApiException e) {
                            MallratbotApplication.logger.error(e.getMessage());
                        }
                    }
                    break;
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
}
