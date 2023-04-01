package sg.edu.nus.pafworkshop21.workshop21.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.pafworkshop21.workshop21.models.News;
import sg.edu.nus.pafworkshop21.workshop21.repositories.NewsRepository;

@Service
public class NewsService {
    
    private static final String URL = "https://newsapi.org/v2/top-headlines";

    @Value("${NEWS_API_KEY}")
    private String key;

    @Autowired
    private NewsRepository newsRepo;

    public List<News> getNews(String country, String username) {

        // queries from the below site
        // https://newsapi.org/v2/top-headlines?country=us&apiKey=f6e86fb3afbe42de833cf2357e68ba4d
        String payload = "";
        String url = UriComponentsBuilder.fromUriString(URL)
            .queryParam("country", country)
            .queryParam("apiKey", key)
            .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url).build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        try {
            resp = template.exchange(req, String.class);
        } catch (Exception ex) {
            System.err.printf("Error: %s\n", ex.getMessage());
            return null;
        }
        payload = resp.getBody();
        Reader strReader = new StringReader(payload);

        JsonReader jr = Json.createReader(strReader);

        JsonObject result = jr.readObject();

        // get likes and set it into list
        List<News> newsList = newsRepo.getLikes(News.create(result), username);


        return newsList;
    }

    public News likeNews(News news, String username) {
        return newsRepo.likeNews(news, username);
    }

    public void unlikeNews(String newsId, String username) {
        newsRepo.unlikeNews(newsId, username);
    }

    public News saveNews(News news) {
        return newsRepo.saveNews(news);
    }

    public List<String> checkPostsLikedByUser(String username) {
        return newsRepo.checkPostsLikedByUser(username);
    }

    public List<News> getNewsByUser(String username) {
        return newsRepo.getNewsByUser(username);
    }

}
