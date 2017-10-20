/**
 * Created by Эм on 07.11.2016.
 */

import Bot.*;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;


public class Main {
    public static void main(String[] args) {
        // создаём объект библиотечного класса
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            // пытаемся зарегистрировать бота
            telegramBotsApi.registerBot(new TgEventBot(new EventWorker()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
