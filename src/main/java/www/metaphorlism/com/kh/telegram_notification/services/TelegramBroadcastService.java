package www.metaphorlism.com.kh.telegram_notification.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import www.metaphorlism.com.kh.telegram_notification.configs.TelegramBroadcasterBot;

@Service
public class TelegramBroadcastService {
    @Autowired
    private TelegramBroadcasterBot telegramBroadcasterBot;
    
    public void sendNotification(String message) {
        telegramBroadcasterBot.broadcastMessage(message);
    }
}
