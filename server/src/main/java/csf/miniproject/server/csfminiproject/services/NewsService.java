package csf.miniproject.server.csfminiproject.services;

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
import csf.miniproject.server.csfminiproject.models.News;
import csf.miniproject.server.csfminiproject.repositories.NewsRepository;

@Service
public class NewsService {
    
    private static final String URL = "https://newsapi.org/v2/top-headlines";
    private static final String SEARCH_NEWS = "https://newsapi.org/v2/everything";

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

        // URI uri = new URI(URL.concat("?country=%s&apiKey=%s".format(country, key)));
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

    public List<News> searchNews(String query, String username) {
        String payload = "";
        String url = UriComponentsBuilder.fromUriString(SEARCH_NEWS)
            .queryParam("q", query)
            .queryParam("apiKey", key)
            .toUriString();

        // URI uri = new URI(URL.concat("?country=%s&apiKey=%s".format(country, key)));
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

    public List<News> selectTopHeadlines(Integer limit, String username) {
        return newsRepo.selectTopHeadlines(limit, username);
    }

}
