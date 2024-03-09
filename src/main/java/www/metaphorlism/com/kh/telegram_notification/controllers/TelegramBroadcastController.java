package www.metaphorlism.com.kh.telegram_notification.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import www.metaphorlism.com.kh.telegram_notification.services.TelegramBroadcastService;
import www.metaphorlism.com.kh.telegram_notification.services.TelegramChatIdService;

@RestController
@RequestMapping("/telegram")
public class TelegramBroadcastController {
    @Autowired
    private TelegramBroadcastService telegramBroadcastService;
    @Autowired
    private TelegramChatIdService telegramChatIdService;
    
    @PostMapping("/broadcast/{chat_id}")
    public void broadcastMessage(@PathVariable("chat_id") String chatId,@RequestBody String message) {
        telegramBroadcastService.sendNotification(chatId,message);
    }
    
    @GetMapping("/chats/{chat_name}/chat-id")
    public String getChatId(@PathVariable("chat_name") String chatName) {
        long chatId = telegramChatIdService.getChatId(chatName);
        
        return "ChatID: " + chatId;
    }
}
