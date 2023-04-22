package csf.miniproject.server.csfminiproject.models;

import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class News {
    
    private String newsId = "";
    private String sourceName;
    private String author = "";
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private DateTime publishedAt;
    private String content;
    private int likes = 0;
    private Boolean liked = false;

    final static String DEFAULT_IMAGE= "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/681px-Placeholder_view_vector.svg.png";

    public String getNewsId() {return newsId;}
    public void setNewsId(String newsId) {this.newsId = newsId;}   
    
    public String getSourceName() {return sourceName;}
    public void setSourceName(String sourceName) {this.sourceName = sourceName;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

    public String getUrlToImage() {return urlToImage;}
    public void setUrlToImage(String urlToImage) {this.urlToImage = urlToImage;}

    public DateTime getPublishedAt() {return publishedAt;}
    public void setPublishedAt(DateTime publishedAt) {this.publishedAt = publishedAt;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public int getLikes() {return likes;}
    public void setLikes(int likes) {this.likes = likes;}

    public Boolean getLiked() {return liked;}
    public void setLiked(Boolean liked) {this.liked = liked;}

    // converting results to jsonData
    public JsonObject toJson() {

        return Json.createObjectBuilder()
            .add("newsId", newsId)
            .add("sourceName", sourceName)
            .add("author", author)
            .add("title", title)
            .add("description", description)
            .add("url", url)
            .add("urlImage", urlToImage)
            .add("publishedAt", getPublishedAt() != null ? getPublishedAt().toString() : "")
            .add("content", content)
            .add("likes", likes)
            .add("liked", liked)
            .build();
    }

    public static String messageToString(News news) {
        String title = news.getTitle();
        String author = news.getAuthor();
        String link = news.getUrl();
        return "Title: %s\nAuthor: %s\nLink: %s".formatted(title, author, link);
    }

    // to get all news
    public static List<News> create(JsonObject jo) {
        JsonArray jArr = jo.getJsonArray("articles");

        // for getting all news, loop through array
        List<News> allNews = new LinkedList<>();
        for (int i = 0; i < jArr.size(); i++) {
            JsonObject news = jArr.getJsonObject(i);
            News article = new News();
            article.setSourceName(news.getJsonObject("source").getString("name"));

            String authors = news.get("author").toString();
            if (authors.contains("null")) {
                article.setAuthor("");
            }
            else {
                article.setAuthor(authors.substring(1, authors.length()-1));
            }
            

            String title = news.get("title").toString();
            article.setTitle(title.substring(1, title.length()-1));

            String description = news.get("description").toString();
            description = description.replaceAll("\\\\", "");
            if (description.contains("null")) {
                article.setDescription("");
            }
            else {
                article.setDescription(description.substring(1,description.length()-1));
            }
            
            String url = news.get("url").toString();
            article.setUrl(url.substring(1,url.length()-1));

            String image = news.get("urlToImage").toString();
            if (image.contains("null")) {
                article.setUrlToImage(DEFAULT_IMAGE);
            }
            else {
                article.setUrlToImage(image.substring(1,image.length()-1));
            }
            

            article.setContent(news.get("content").toString());
            article.setPublishedAt(DateTime.parse(news.getString("publishedAt")));
            allNews.add(article);
            }
        return allNews;
        }

    public static News create(SqlRowSet rs) {
        News news = new News();
        news.setNewsId(rs.getString("newsId"));
        news.setAuthor(rs.getString("author"));
        news.setTitle(rs.getString("title"));
        news.setSourceName(rs.getString("sourceName"));
        news.setUrl(rs.getString("url"));
        news.setUrlToImage(rs.getString("urlImage"));
        news.setDescription(rs.getString("description"));
        news.setContent(rs.getString("content"));
        news.setPublishedAt(new DateTime(
            DateTimeFormat.forPattern("dd/MM/yyyy")
                    .parseDateTime(rs.getString("publishedAt"))));
        news.setPublishedAt(DateTime.now());
        return news;
    }

    
    // for controller method to save only one news
    public static News save(JsonObject jo) {
        News news = new News();
        news.setSourceName(jo.getString("sourceName"));
        news.setAuthor(jo.getString("author"));
        news.setContent(jo.getString("content"));

        String description = jo.getString("description");
        description = description.replaceAll("\\\\", "");
        news.setDescription(description);
        String dateTime = jo.getString("publishedAt");
        dateTime = dateTime.replace("Z", "").substring(0, dateTime.length()-5);
        news.setPublishedAt(DateTime.parse(dateTime));
        news.setTitle(jo.getString("title"));
        news.setUrl(jo.getString("url"));
        news.setUrlToImage(jo.getString("urlImage"));


        return news;
    }

}

    

