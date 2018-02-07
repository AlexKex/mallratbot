package ru.apr.messaging;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;

@Component
public class SingleMessage extends AbstractMessage implements Sendable {

}
