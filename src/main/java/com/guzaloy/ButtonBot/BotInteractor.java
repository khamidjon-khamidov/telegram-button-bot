package com.guzaloy.ButtonBot;

import com.guzaloy.ButtonBot.base.BotInteractorBase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BotInteractor extends BotInteractorBase {
    final String channelOwner;
    final Long channelChatId;

    BotInteractor(
            @Value("${bot.token}") String token,
            @Value("${bot.username}") String username,
            @Value("${channel.owner}") String channelOwner,
            @Value("${channel.chatId}") Long channelChatId
    ) {
        super(token, username);
        this.channelOwner = channelOwner;
        this.channelChatId = channelChatId;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Got message update: " + update);
        if (update.hasMessage() && update.getMessage().getFrom() != null && update.getMessage().getFrom().getUserName().equals(channelOwner)) {
            System.out.println("Above message is from owner");
            try {
                var message = handleOwnerMessage(update);
                if (message != null){
                    sendApiMethod(message);
                }
            } catch (TelegramApiException exception) {
                System.out.println("ERROR: " + exception);;
            }
        }
    }

    Message prevMessage = null;
    BotApiMethod handleOwnerMessage(Update update) {
        if (prevMessage == null) {
            SendMessage response = new SendMessage();
            response.setChatId(update.getMessage().getChatId());
            prevMessage = update.getMessage();
            response.setText("Xabar ko'chirildi. Tugma nomini kiriting");
            return response;
        }

        if (update.getMessage().getText().equalsIgnoreCase("cancel")){
            SendMessage response = new SendMessage();
            response.setChatId(update.getMessage().getChatId());
            prevMessage = null;
            response.setText("Xabar o'chirildi");
            return response;
        }

        CopyMessage response = new CopyMessage();
        response.setChatId(channelChatId);
        response.setMessageId(prevMessage.getMessageId());
        response.setFromChatId(prevMessage.getChatId());
        addButton(response, update.getMessage().getText());

        prevMessage = null;
        return response;
    }

    private void addButton(CopyMessage response, String buttonName) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        InlineKeyboardButton pizzaButton = new InlineKeyboardButton();
        pizzaButton.setText(buttonName);
        pizzaButton.setUrl("t.me/" + channelOwner);
        keyboard.add(List.of(pizzaButton));


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        response.setReplyMarkup(inlineKeyboardMarkup);
    }
}






















