package com.guzaloy.ButtonBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SuppressWarnings("ALL")
@SpringBootApplication
public class ButtonBotApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ButtonBotApplication.class, args);

		try {
			BotInteractor botInteractor = context.getBean(BotInteractor.class);

			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

			telegramBotsApi.registerBot(botInteractor);
		} catch (TelegramApiException ex) {
			ex.printStackTrace();
		}
	}

}
