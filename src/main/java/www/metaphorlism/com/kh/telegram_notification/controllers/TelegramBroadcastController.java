package www.metaphorlism.com.kh.telegram_notification.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import www.metaphorlism.com.kh.telegram_notification.services.TelegramBroadcastService;

@RestController
@RequestMapping("/telegram")
public class TelegramBroadcastController {
    @Autowired
    private TelegramBroadcastService telegramBroadcastService;
    
    @PostMapping("/broadcast")
    public void broadcastMessage(@RequestBody String message) {
        telegramBroadcastService.sendNotification(message);
    }
}
