package www.metaphorlism.com.kh.telegram_notification.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class TelegramChatIdService {
    
    @Value("${telegram.bot.token}")
    private String botToken;
    
    private final WebClient webClient;
    
    public TelegramChatIdService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    public Long getChatId(String chatName) {
        String baseUrl = "https://api.telegram.org/bot" + botToken;
        
        return webClient
                .get()
                .uri(baseUrl + "/getUpdates")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    // Parse JSON response and extract chat ID
                    return parseChatIdFromJson(response,chatName);
                })
                .block();
    }
    
    private Long parseChatIdFromJson(String jsonResponse,String chatName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(jsonResponse, new TypeReference<Map<String,Object>>() {});
            // Assuming the JSON structure contains a "result" field which is a list of updates
            List<Map<String, Object>> updates = (List<Map<String, Object>>) jsonMap.get("result");
            
            if (!updates.isEmpty()) {
                for(Map<String,Object> map : updates) {
                    
                    if(map.get("my_chat_member") == null) continue;
                    
                    Map<String,Object> message = (Map<String,Object>) map.get("my_chat_member");
                    Map<String,Object> chat = (Map<String,Object>) message.get("chat");
                    
                    if(isGroupType(chat)) {
                        if(chat.get("title").toString().equalsIgnoreCase(chatName)) {
                            return Long.parseLong(chat.get("id").toString());
                        }
                    }else if(isChannelType(chat)) {
                        if(chat.get("username").toString().equalsIgnoreCase(chatName)) {
                            return Long.parseLong(chat.get("id").toString());
                        }
                    }
                }
            }
            
            return 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
    
    private boolean isGroupType(Map<String,Object> chat) {
        return chat.get("type").equals("group");
    }
    
    private boolean isChannelType(Map<String,Object> chat) {
        return chat.get("type").equals("channel");
    }
}

