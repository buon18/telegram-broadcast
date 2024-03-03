package www.metaphorlism.com.kh.telegram_notification.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {
    @Value("${telegram.bot.token}")
    private String botToken;
    
    @Value("${telegram.bot.username}")
    private String botUsername;
    
    @Value("${telegram.chat-id}")
    private String chatId;
    
    @Bean
    public TelegramBroadcastBot setupTelegramBot() {
        return new TelegramBroadcastBot(botToken, botUsername, chatId);
    }
}

