package Bot; /**
 * Created by Эм on 02.11.2016.
 */

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


public class TgEventBot extends TelegramLongPollingBot {
    private final String descriptionBot = "Привет!\n\nЯ бот, которые держит тебя в курсе основных интересных событий в Москве.";
    private final String startBot = "Доступные команды:\n\n /help – справка по боту\n /start – список доступных команд" +
            "\n/search – поиск ближайших событий";


    SendMessage _sendMessage;

    IEventWorker _eventWorker;

    public TgEventBot(IEventWorker newsReader){
        _eventWorker = newsReader;

        InitializeCompomemts();
    }

    private void InitializeCompomemts() {
        _sendMessage = new SendMessage();
        _sendMessage.enableMarkdown(true);

    }

    public void onUpdateReceived(Update update) {
        //если в апдейте есть сообщение
        if (update.hasMessage()) {
            Message message = update.getMessage();
            //проверяем, есть ли в сообщении текст
            if (message.hasText()) {
                //создаем объект с ответом
                SendMessage sendMessageRequest = new SendMessage();
                sendMessageRequest.setChatId(message.getChatId().toString());
                if (message.getText().equals("/help")){
                    sendMessageRequest.setText(startBot);
                    try {
                        sendMessage(sendMessageRequest);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (message.getText().equals("/start")){
                    sendMessageRequest.setText(descriptionBot);
                    try {
                        sendMessage(sendMessageRequest);
                    } catch (TelegramApiException e){
                        e.printStackTrace();
                    }
                }
                if (message.getText().startsWith("/search")) {
                    String sss = _eventWorker.GetEvent(message.getText());

                    SendMessage newMessage = new SendMessage();
                    newMessage.setText(sss)
                            .setChatId(message.getChatId().toString());
                    try {
                        sendMessage(newMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

    }

    public String getBotUsername() {
        return "TgEventMoscowBot";
    };

    public String getBotToken() {
        return "191828221:AAH4eNE8GasoaKoUo-VMEulU_7zhSyEIqd8";
    };

    private void sendMessage(Message message, String text) {
        _sendMessage.setChatId(message.getChatId().toString());
        _sendMessage.setReplyToMessageId(message.getMessageId());
        _sendMessage.setText(text);

        try {
            sendMessage(_sendMessage);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

}
