package www.metaphorlism.com.kh.telegram_notification.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import www.metaphorlism.com.kh.telegram_notification.configs.TelegramBroadcastBot;

@Service
public class TelegramBroadcastService {
    @Autowired
    private TelegramBroadcastBot telegramBroadcasterBot;
    
    public void sendNotification(String chatId,String message) {
        telegramBroadcasterBot.broadcastMessage(chatId,message);
    }
}
