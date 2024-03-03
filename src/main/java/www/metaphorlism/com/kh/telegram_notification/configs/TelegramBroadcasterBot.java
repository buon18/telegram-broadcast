package www.metaphorlism.com.kh.telegram_notification.configs;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBroadcasterBot extends TelegramLongPollingBot {
    
    private String botToken;
    private String botUsername;
    private String chatId;
    
    public TelegramBroadcasterBot(String botToken, String botUsername, String chatId) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.chatId = chatId;
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        // Handle incoming messages or events here
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
