package ru.apr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import ru.apr.controller.BotController;
import ru.apr.util.BeanFileLoader;

@SpringBootApplication
public class MallratbotApplication {

	public static Logger logger = LoggerFactory.getLogger(MallratbotApplication.class.getName());

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(MallratbotApplication.class, args);
		BotController mrbc = context.getBean(BotController.class);
	}
}
