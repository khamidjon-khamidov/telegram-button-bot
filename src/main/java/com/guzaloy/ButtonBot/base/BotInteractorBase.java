
package com.guzaloy.ButtonBot.base;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public abstract class BotInteractorBase extends TelegramLongPollingBot {
    private final String username;
    public BotInteractorBase(String token, String username) {
        super(token);
        this.username = username;
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}