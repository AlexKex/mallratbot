package ru.apr.entity.dialogue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Answer {

    @Autowired
    ApplicationContext context;

    private String greetings;
    private String parseMessageError;

    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String greetings) {
        this.greetings = greetings;
    }

    public String getParseMessageError() {
        return parseMessageError;
    }

    public void setParseMessageError(String parseMessageError) {
        this.parseMessageError = parseMessageError;
    }

}
