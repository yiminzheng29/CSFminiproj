package csf.miniproject.server.csfminiproject.services;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import csf.miniproject.server.csfminiproject.models.News;

import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final String URL = "https://newsapi.org/v2/everything";

    @Override
    public String getBotUsername() {
        return "this_newsapp_bot";
    }

    @Override
    public String getBotToken() {
        return "6134631395:AAGQAP2Aq19zJtfT_5nnDLxNDgjMJymzfrA";
    }

    @Override
    public void onUpdateReceived(Update update) {

        String query = "";
        String command = update.getMessage().getText();
        String payload = "";
        if(command.startsWith("/search")){
            query = command.substring(8, command.length());

            // pass query to API and get results
            try {
                String url = UriComponentsBuilder.fromUriString(URL)
                    .queryParam("q", URLEncoder.encode(query, "UTF-8"))
                    .queryParam("apiKey", "f6e86fb3afbe42de833cf2357e68ba4d")
                    .toUriString();

                RequestEntity<Void> req = RequestEntity.get(url).build();

                RestTemplate template = new RestTemplate();
                ResponseEntity<String> resp;
                resp = template.exchange(req, String.class);

                payload = resp.getBody();
                Reader strReader = new StringReader(payload);
            
                JsonReader jr = Json.createReader(strReader);
            
                JsonObject result = jr.readObject();
            
                List<News> results = new LinkedList<>();
                results = News.create(result);

                News latestNews = results.get(0); 
                String reply = News.messageToString(latestNews);

                SendMessage response = new SendMessage();
                response.setChatId(update.getMessage().getChatId().toString());
                response.setText(reply);

                
                execute(response);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
