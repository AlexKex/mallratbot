package ru.apr;

import org.glassfish.jersey.jaxb.internal.XmlCollectionJaxbProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
public class BotController {

    @Autowired
    ApplicationContext context;

    @PostConstruct
    private void initialize(){
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(context.getBean(MallRatBot.class));
        } catch (TelegramApiException e) {
            MallratbotApplication.logger.error(e.getMessage());
        }
    }

    void start(){

    }
}
