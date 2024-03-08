# Listen to events from Telegram Channels/Groups
On our previous blog post, we had done configuring and implementing for broadcasting notifications to a specific channel/group.

[Previous Blog](https://blogs.metaphorlism.com/blogs/65e44d29d39cf954960690a1)

So within this blog we will only focus on `onUpdateReceived()` method which listens the events from channels / groups.

## `TelegramBroadcastBot`
This is our previous `TelegramBroadcastBot` class before implementing the `onUpdateReceived()` method.

```java
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

  public void broadcastMessage(String message) {
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
```

## Implementation for `onUpdateReceived()` method

```java
@Override
public void onUpdateReceived(Update update) {
    if(update.getChannelPost().isChannelMessage() && update.hasChannelPost()) {
        String messageText = update.getChannelPost().getText();
        Long chatId = update.getChannelPost().getChatId();
        
        // get sender username
        String sender = update.getChannelPost().getChat().getUserName();
        
        // get received datetime and format it
        Integer dateInTimestamp = update.getChannelPost().getDate();
        Instant instant = Instant.ofEpochSecond(dateInTimestamp);
        String receivedDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toString()
        .replace("T"," ");
        
        // Send the response back to the channel
        broadcastMessage(String.valueOf(chatId),"received: " + receivedDate + "\nreplied to " + sender + "\nmessage: " + messageText);
    }
}
```

- We first check if the update contains a channel post `update.hasChannelPost()` and if it is indeed a channel message `update.getChannelPost().isChannelMessage()` which returns a boolean.
- We then extract the message text, chat ID, and sender's username (assuming it's available).
- We convert the timestamp of the received date to a LocalDateTime object and format it.
- Finally, we prepare the response message including the formatted date, sender's username, and message text, and send it back to the channel using the broadcastMessage method.

However, we still need to modify our `TelegramBotConfig` class. Below is the implementation for it.


## `TelegramBotConfig`

```java
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
        TelegramBroadcastBot telegramBot = new TelegramBroadcastBot(botToken, botUsername, chatId);
        
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        
        return telegramBot;
    }
}
```

- Create Telegram Bot Instance: It creates an instance of the TelegramBroadcastBot class, passing the required parameters `botToken`, `botUsername`, and `chatId` to its constructor.

- Register Bot with Telegram Bots API: It creates an instance of TelegramBotsApi, which is the entry point for interacting with the Telegram Bots API. Then, it calls the registerBot method of TelegramBotsApi, passing the telegramBot instance as an argument. This method registers the bot with the Telegram Bots API, enabling it to receive updates and interact with users.

- Return Bot Instance: Finally, it returns the telegramBot instance. This allows the bot instance to be injected into other Spring beans and components, allowing them to interact with the bot as needed.


#### With all of these implementations you are good to go with TelegramBot to broadcast notifications and listen to events from channels or groups.

## Previous related contents:
- [Configurations and implementations to get started with TelegramBot to broadcast notification](https://blogs.metaphorlism.com/blogs/65e44d29d39cf954960690a1)

## What's next ?
In our next blog post, we will improve this application:
- Handle the exception and send notification to a specific group/channel whenever the code runs into the Exceptions.
- Create another endpoint to retrieve the ChatID by Group/Channel name.

**Stay tune and stay safe from 🪲🪲**

## GitHub Repository

[Telegram Notification Broadcast](https://github.com/metaphorlism/telegram-broadcast/tree/feature/telegram-broadcast)
