# Broadcast Notification To Telegram

## Table of Contents
1. [Create Telegram Bot](#create-telegram-bot)
2. [Dependency](#required-dependency)
3. [Configuration](#configurations)
4. [Broadcast Bot](#telegrambroadcastbot)
5. [Bot Config](#telegrambotconfig)
6. [Broadcast Service](#telegrambroadcastservice)
7. [Broadcast Controller](#telegrambroadcastcontroller)
8. [How to retrieve ChatID ?](#how-to-retrieve-the-id-of-the-chat-)
9. [What's next ?](#whats-next-)
10. [Repository](#github-repository)

## Create Telegram Bot
First thing first we need to create a Telegram bot with [BotFather](https://telegram.me/BotFather) with below steps:

- Start a Chat with BotFather: Open Telegram and search for "BotFather". Start a chat with BotFather by clicking on the "Start" button.

- Create a New Bot: Once you're chatting with BotFather, send the command /newbot. BotFather will ask you to choose a name for your bot.

- Choose a Name for Your Bot: After sending the /newbot command, BotFather will ask you to choose a name for your bot. This name will be displayed in Telegram chats and contact lists.

- Choose a Username for Your Bot: After choosing a name, BotFather will ask you to choose a username for your bot. This username must end in "bot" and be unique. For example, if you choose the name "MyNotificationBot", your bot's username could be something like "MyNotificationBot_bot".

- Get Your Bot Token: After choosing a username, BotFather will generate a token for your bot. This token is like a secret key that your bot uses to authenticate with the Telegram Bot API. Keep this token secure, as it allows anyone with access to it to control your bot.

- Set Up Privacy Mode (Optional): BotFather may ask you if you want to enable privacy mode for your bot. This option allows you to restrict who can add your bot to groups. You can choose whether to enable or disable privacy mode based on your bot's requirements.

- Your Bot Is Ready: Once you've completed the setup process, BotFather will provide you with a message confirming that your bot has been created. It will also give you a link to your bot's profile and its API token.

### After the bot is successfully created then we can start implementing the code

## Required Dependency

```xml
<dependency>
    <groupId>org.telegram</groupId>
    <artifactId>telegrambots</artifactId>
    <version>5.3.0</version>
</dependency>
```

## Configurations
In `application.yaml`( you can differentiate the configurations )

```yaml
server:
  port: 8080

spring:
  application: 'YOUR-APPLICATION-NAME'

telegram:
  bot:
    token: 'YOUR-BOT-TOKEN'
    username: 'YOUR-BOT-USERNAME'
  chat-id: 'YOUR-CHAT-ID'
```

## `TelegramBroadcastBot`
This class is a subclass of `TelegramLongPollingBot`, which is part of the Telegram Bots API. It represents a bot that listens for updates from Telegram and can send messages.

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

### Here's what each part of the class does:

- Constructor: Initializes the botToken, botUsername, and chatId fields with the values provided during instantiation.

- onUpdateReceived Method: This method is called whenever the bot receives an update from Telegram. You can implement custom logic here to handle incoming messages or events.

- getBotUsername Method: Returns the username of the bot.

- getBotToken Method: Returns the token of the bot.

- broadcastMessage Method: Sends a message to specific chat identified by chatId.

## `TelegramBotConfig`
This class is a configuration class responsible for creating and configuring a bean of the `TelegramBroadcastBot` class. It reads properties from the application configuration (in our case is `application.yaml`) using `@Value` annotations and injects them into the `TelegramBroadcastBot` bean.

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
        return new TelegramBroadcastBot(botToken, botUsername, chatId);
    }
}
```

## `TelegramBroadcastService`
This class is a service class responsible for sending notifications via a Telegram bot with our `TelegramBroadcastBot` class.

```java
@Service
public class TelegramBroadcastService {
    @Autowired
    private TelegramBroadcastBot telegramBroadcastBot;
    
    public void sendNotification(String message) {
        telegramBroadcastBot.broadcastMessage(message);
    }
}
```

## `TelegramBroadcastController`
This class is just a controller class which is used to expose endpoint so that we can broadcast message with API request.

```java
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
```
With all of these implementations you are good to go with TelegramBot to broadcast notifications to a specific group or channel by identifying the ID of the chat.

## How to retrieve the ID of the Chat ?
You can request to this API to retrieve the chat id
`curl https://api.telegram.org/bot<your-bot-token>/getUpdates
`

**Replace `<your-bot-token>` with your bot's token**

## What's next ?
In our next blog post, we will improve this application:
- Handle the exception and send notification to a specific group/channel whenever the code runs into the Exceptions.
- Create another endpoint to retrieve the ChatID by Group/Channel name.
- [Listen to any updates on the Telegram Group/Channel then handle the incoming requests](https://blogs.metaphorlism.com/blogs/65eae81ed39cf954960690c7)

**Stay tune and stay safe from 🪲🪲**

## GitHub Repository

[Telegram Notification Broadcast](https://github.com/metaphorlism/telegram-broadcast/tree/feature/telegram-broadcast)
