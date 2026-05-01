package com.example.home_thermostat_api.telegram;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.example.home_thermostat_api.dto.LoginRequest;
import com.example.home_thermostat_api.dto.LoginResponse;
import com.example.home_thermostat_api.dto.RegisterRequest;
import com.example.home_thermostat_api.dto.RegisterResponse;
import com.example.home_thermostat_api.service.AuthService;

import jakarta.annotation.PostConstruct;

@Component
public class ThermostatTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;

    @Autowired
    private AuthService authService;

    @Autowired
    private TelegramKeyboards telegramKeyboards;

    @Autowired
    private TelegramService telegramService;

    private final Map<Long, UserState> userStates = new HashMap<>();
    private final Map<Long, String> userTokens = new HashMap<>();
    private final Map<Long, String[]> tempData = new HashMap<>();
    private final Map<Long, String> authenticatedUsers = new HashMap<>();

    public ThermostatTelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("✅ Бот зарегистрирован: " + botUsername);
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка регистрации бота: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();

            if (messageText.equals("/start")) {
                userStates.put(chatId, UserState.START);
                sendMessageWithKeyboard(chatId,
                        "👋 Привет, " + userName + "!\nВыберите действие:",
                        telegramKeyboards.getAuthKeyboard());
                return;
            }

            if (messageText.equals("/logout")) {
                userStates.remove(chatId);
                userTokens.remove(chatId);
                tempData.remove(chatId);
                sendMessageWithKeyboard(chatId, "👋 Вы вышли из системы.", telegramKeyboards.getAuthKeyboard());
                return;
            }

            UserState state = userStates.getOrDefault(chatId, UserState.START);

            switch (state) {
                case START -> handleStartState(chatId, messageText);
                case AWAITING_USERNAME -> handleLoginUsername(chatId, messageText);
                case AWAITING_PASSWORD -> handleLoginPassword(chatId, messageText);
                case AWAITING_REGISTER_USERNAME -> handleRegisterUsername(chatId, messageText);
                case AWAITING_REGISTER_EMAIL -> handleRegisterEmail(chatId, messageText);
                case AWAITING_REGISTER_PASSWORD -> handleRegisterPassword(chatId, messageText);
                case LOGGED_IN -> handleLoggedIn(chatId, messageText);
            }
        }
    }

    private void handleStartState(Long chatId, String text) {
        switch (text) {
            case "🔐 Войти" -> {
                userStates.put(chatId, UserState.AWAITING_USERNAME);
                sendMessage(chatId, "Введите ваш username:");
            }
            case "📝 Регистрация" -> {
                userStates.put(chatId, UserState.AWAITING_REGISTER_USERNAME);
                sendMessage(chatId, "Придумайте username:");
            }
            default -> sendMessageWithKeyboard(chatId, "Выберите действие:", telegramKeyboards.getAuthKeyboard());
        }
    }

    private void handleLoginUsername(Long chatId, String username) {
        tempData.computeIfAbsent(chatId, k -> new String[3])[0] = username;
        userStates.put(chatId, UserState.AWAITING_PASSWORD);
        sendMessage(chatId, "Введите пароль:");
    }

    private void handleLoginPassword(Long chatId, String password) {
        String username = tempData.get(chatId)[0];

        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            ResponseEntity<LoginResponse> response = authService.loginForBot(loginRequest);

            System.out.println("🔍 Бот: ответ = " + response.getStatusCode());

            if (response.getBody() != null && response.getBody().isLogged()) {
                authenticatedUsers.put(chatId, username);
                userTokens.put(chatId, "bot_authenticated");
                userStates.put(chatId, UserState.LOGGED_IN);
                tempData.remove(chatId);
                sendMessageWithKeyboard(chatId,
                        "✅ Вход выполнен! Добро пожаловать, " + username + "!",
                        telegramKeyboards.getMainKeyboard());
            } else {
                sendMessageWithKeyboard(chatId,
                        "❌ Неверный логин или пароль.",
                        telegramKeyboards.getAuthKeyboard());
                userStates.put(chatId, UserState.START);
            }
        } catch (Exception e) {
            System.err.println("❌ Бот: ошибка входа: " + e.getMessage());
            sendMessageWithKeyboard(chatId,
                    "❌ Неверный логин или пароль. Попробуйте снова.",
                    telegramKeyboards.getAuthKeyboard());
            userStates.put(chatId, UserState.START);
        }
    }

    private void handleRegisterUsername(Long chatId, String username) {
        tempData.computeIfAbsent(chatId, k -> new String[3])[0] = username;
        userStates.put(chatId, UserState.AWAITING_REGISTER_EMAIL);
        sendMessage(chatId, "Введите email:");
    }

    private void handleRegisterEmail(Long chatId, String email) {
        tempData.get(chatId)[1] = email;
        userStates.put(chatId, UserState.AWAITING_REGISTER_PASSWORD);
        sendMessage(chatId, "Придумайте пароль:");
    }

    private void handleRegisterPassword(Long chatId, String password) {
        String username = tempData.get(chatId)[0];
        String email = tempData.get(chatId)[1];

        try {
            RegisterRequest request = new RegisterRequest(username, email, password);
            ResponseEntity<RegisterResponse> response = authService.registerForBot(request);

            if (response.getStatusCode().is2xxSuccessful()) {
                authenticatedUsers.put(chatId, username);
                userTokens.put(chatId, "bot_authenticated");
                userStates.put(chatId, UserState.LOGGED_IN);
                tempData.remove(chatId);
                sendMessageWithKeyboard(chatId,
                        "✅ Регистрация успешна! Добро пожаловать, " + username + "!",
                        telegramKeyboards.getMainKeyboard());
            }
        } catch (Exception e) {
            sendMessageWithKeyboard(chatId,
                    "❌ Ошибка: " + e.getMessage(),
                    telegramKeyboards.getAuthKeyboard());
            userStates.put(chatId, UserState.START);
        }
    }

    private void handleLoggedIn(Long chatId, String text) {
        switch (text) {
            case "🏠 Мои дома" -> {
                String username = authenticatedUsers.get(chatId);
                if (username == null) {
                    sendMessage(chatId, "❌ Вы не авторизованы.");
                    return;
                }
                String homesList = telegramService.getUserHomes(username);
                sendMessage(chatId, homesList);
            }

            case "🌡️ Температура" -> {
                String username = authenticatedUsers.get(chatId);
                if (username == null) {
                    sendMessage(chatId, "❌ Вы не авторизованы.");
                    return;
                }
                String tempMessage = telegramService.getTemperatures(username);
                sendMessage(chatId, tempMessage);
            }

            case "📖 Инструкция" -> {
                String instruction = """
                        📖 *Инструкция по использованию Home Thermostat*

                        🌐 *Web-интерфейс (Swagger):*
                        %s/swagger-ui/index.html

                        Через Swagger можно:
                        • 🏠 Создавать и удалять дома
                        • 🚪 Добавлять и удалять комнаты
                        • 🌡️ Устанавливать температуру
                        • 📊 Скачивать Excel отчёты
                        • 📥 Загружать данные из CSV/Excel

                        🤖 *Через бота доступно:*
                        • Просмотр домов
                        • Просмотр текущей температуры
                        """.formatted("http://localhost:8080");

                sendMessage(chatId, instruction);
            }

            case "🚪 Выйти" -> {
                authenticatedUsers.remove(chatId);
                userStates.remove(chatId);
                userTokens.remove(chatId);
                sendMessageWithKeyboard(chatId, "Вы вышли из системы.", telegramKeyboards.getAuthKeyboard());
            }
            default -> sendMessageWithKeyboard(chatId, "Выберите действие:", telegramKeyboards.getMainKeyboard());
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageWithKeyboard(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
