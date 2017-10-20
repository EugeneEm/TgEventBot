package Bot;

/**
 * Created by Эм on 02.11.2016.
 */

import org.json.JSONObject;
import org.json.JSONArray;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import static com.sun.xml.internal.ws.api.message.Packet.Status.Response;


public class EventWorker implements IEventWorker {
    Client client;

    /**
     * Конструктор по умолчанию
     */
    public EventWorker(){
        client = ClientBuilder.newClient();
    }

    /**
     * Делает запрос к API и возвращает строку для печати
     * @param query
     * @return
     */
    public String GetEvent (String query){
        //город и дата
        String city="Москва";
        Date nowDate = new Date();

        try {
            // добавляем адрес до API
            /* Категории:
                ИТ и интернет 452,
                Бизнес 217,
                Театры 459,
                Инострынные языки 382,
                Концерты 460,
             */
            query="https://api.timepad.ru/v1/events.json?limit=5&skip=0&cities=Москва&fields=location&sort=+starts_at";
            // преобразуем к "нормальному" URI (заменяем пробелы на '%20')
            URI uri = new URI(query.replaceAll(" ","%20"));
            // делаем запрос
            Response response = client.target(uri).request().get();
            return ParseResponse(response.readEntity(String.class));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Парсит результаты, возвращает "готовую" для печати строку
     * @param response - JSON ответ
     * @return - строка для печати
     */
    private String ParseResponse(String response){
        String answer = "";
        // сначала пытаемся извлечь "основной" результат
        JSONObject jsonObject = new JSONObject(response);
        JSONArray events = jsonObject.getJSONArray("values");

        for (int i = 0; i < Math.min(events.length(), 5); ++i) {
            answer += "Событие: " + events.getJSONObject(i).getString("name") + "\n";
            answer += "Начало: " + events.getJSONObject(i).getString("starts_at") + "\n";
            answer += events.getJSONObject(i).getString("url") + "\n\n";
        }
        return answer;
    }
}
