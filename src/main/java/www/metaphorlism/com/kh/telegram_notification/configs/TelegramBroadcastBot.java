package www.metaphorlism.com.kh.telegram_notification.configs;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TelegramBroadcastBot extends TelegramLongPollingBot {
    
    private String botToken;
    private String botUsername;
    private String chatId;
    
    public TelegramBroadcastBot(String botToken, String botUsername, String chatId) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.chatId = chatId;
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        if(update.getChannelPost().isChannelMessage() && update.hasChannelPost()) {
            String messageText = update.getChannelPost().getText();
            Long chatId = update.getChannelPost().getChatId();
            
            // get sender username
            String sender = update.getChannelPost().getChat().getUserName();
            
            // get received datetime
            Integer dateInTimestamp = update.getChannelPost().getDate();
            Instant instant = Instant.ofEpochSecond(dateInTimestamp);
            String receivedDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toString()
                    .replace("T"," ");
            
            // Send the response back to the channel
            broadcastMessage(String.valueOf(chatId),"received: " + receivedDate + "\nreplied to " + sender + "\nmessage: " + messageText);
        }
    }
    
    @Override
    public String getBotUsername() {
        return botUsername;
    }
    
    @Override
    public String getBotToken() {
        return botToken;
    }
    
    public void broadcastMessage(String chatId, String message) {
        this.chatId = chatId;
        
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
