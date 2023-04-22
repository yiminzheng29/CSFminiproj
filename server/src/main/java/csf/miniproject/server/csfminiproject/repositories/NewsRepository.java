package csf.miniproject.server.csfminiproject.repositories;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import csf.miniproject.server.csfminiproject.models.News;
import static csf.miniproject.server.csfminiproject.repositories.Queries.*;

@Repository
public class NewsRepository {
    
    @Autowired
    private JdbcTemplate template;

    @Transactional(rollbackFor = NewsException.class)
    public List<News> getLikes(List<News> news, String username) {
        
        SqlRowSet rs = template.queryForRowSet(SQL_CHECK_POSTS_LIKED_BY_USER, username);
        List<String> likedPosts = new LinkedList<>();
            while (rs.next()) {
                likedPosts.add(rs.getString("newsId"));
            };

        for (News article: news) {
            article.setLiked(true);
            SqlRowSet result = template.queryForRowSet(SQL_CHECK_IF_NEWS_EXIST, article.getTitle());
            if (result.next()) {
                String newsId = result.getString("newsId");
                article.setNewsId(newsId);
    
                SqlRowSet likesResult  = template.queryForRowSet(SQL_GET_LIKES, newsId);
                if (likesResult.next()) {
                    Integer likes = likesResult.getInt("likes");
                    article.setLikes(likes);
                    
                }
            }
            if (!likedPosts.contains(article.getNewsId())) {
                article.setNewsId("");
                article.setLiked(false);
            }

        }
        return news;
    }

    @Transactional(rollbackFor = NewsException.class)
    public List<String> checkPostsLikedByUser(String username) {
        SqlRowSet rs = template.queryForRowSet(SQL_CHECK_POSTS_LIKED_BY_USER, username);
        List<String> results = new LinkedList<>();
        
        if (rs.next()) {
            results.add(rs.getString("newsId"));
        };
        return results;
    }

    @Transactional(rollbackFor = NewsException.class)
    public News likeNews(News news, String username) {
            // check if news exists in db
        SqlRowSet result = template.queryForRowSet(SQL_CHECK_IF_NEWS_EXIST, news.getTitle());
            
        String newsId = "";
        Integer likes = 0;
    
            if (result.next()) {
                newsId = result.getString("newsId");
    
                SqlRowSet likesResult  = template.queryForRowSet(SQL_GET_LIKES, newsId);
                // likes += likesResult.getInt("likes");
                if (likesResult.next()) {
                    likes = likesResult.getInt("likes");
                }
                else {
                    // initialize likes
                    template.update(SQL_SAVE_LIKES_RECORD, newsId, likes);
                    System.out.println(likes);
                }
                // update likes record
                likes += 1;
                template.update(SQL_UPDATE_LIKES_RECORD, likes, newsId);
    
                // update user's record
                template.update(SQL_SAVE_NEWS_RECORD, newsId, username);
            }
            else {
                newsId = UUID.randomUUID().toString().substring(0,6);
    
                // persist data into news table
                template.update(SQL_SAVE_NEWS, newsId, news.getTitle(), news.getAuthor(), news.getSourceName(), 
                new Timestamp(news.getPublishedAt().toDateTime().getMillis()), news.getUrl(), news.getUrlToImage(), news.getDescription(), news.getContent());
    
                // persist data into newsRecord table
                template.update(SQL_SAVE_NEWS_RECORD, newsId, username);
    
                // initialize likes
                likes +=1;
                template.update(SQL_SAVE_LIKES_RECORD, newsId, likes);
    
            }

            news.setNewsId(newsId);
            news.setLikes(likes);
            news.setLiked(true);
            
            return news;
        }

    @Transactional(rollbackFor = NewsException.class)
    public News saveNews(News news) {
        // check if news exists in db
        SqlRowSet result = template.queryForRowSet(SQL_CHECK_IF_NEWS_EXIST, news.getTitle());
        
        String newsId = "";

        if (result.next()) {
            newsId = result.getString("newsId");
        }
        else {
            newsId = UUID.randomUUID().toString().substring(0,6);

            // persist data into news table
            template.update(SQL_SAVE_NEWS, newsId, news.getTitle(), news.getAuthor(), news.getSourceName(), 
            new Timestamp(news.getPublishedAt().toDateTime().getMillis()), news.getUrl(), news.getUrlToImage(), news.getDescription(), news.getContent());

        }
        news.setNewsId(newsId);
        
        return news;
    }

    @Transactional(rollbackFor = NewsException.class)
    public void unlikeNews(String newsId, String username) {

        // update likes record table in sql
        Integer likes = 0;

        // first, get likes from existing likes record
        SqlRowSet rs = template.queryForRowSet(SQL_GET_LIKES, newsId); 
        if (rs.next())
            likes += rs.getInt("likes");

        // update likes in likes record
        template.update(SQL_UPDATE_LIKES_RECORD, likes-1, newsId);

        // update news record table in sql
        template.update(SQL_DELETE_NEWS_RECORD, username, newsId);

    }

    @Transactional(rollbackFor = NewsException.class)
    public List<News> getNewsByUser(String username) {
        List<News> newsByUser = new LinkedList<>();
        List<String> records = new LinkedList<>();

        // get all records by user in news record first
        SqlRowSet rs = template.queryForRowSet(SQL_CHECK_POSTS_LIKED_BY_USER, username);
        // append all newsRecord id into a string first
        while (rs.next()) {
            records.add(rs.getString("newsId"));
        }

        for (String newsId: records) {
            SqlRowSet result = template.queryForRowSet(SQL_GET_USER_NEWS, newsId);
            if (result.next()) {
                News news = News.create(result);
                news.setLiked(true);

                SqlRowSet likes = template.queryForRowSet(SQL_GET_LIKES, newsId);
                if (likes.next()) {
                    news.setLikes(likes.getInt("likes"));
                }
                newsByUser.add(news);
            }
        }
        return newsByUser;
    }

    @Transactional(rollbackFor = NewsException.class)
    public List<News> selectTopHeadlines(Integer limit, String username) {
        List<News> results = new LinkedList<>();
        List<String> newsId = new LinkedList<>();
        List<String> postsLiked = new LinkedList<>();
        SqlRowSet rs = template.queryForRowSet(SQL_GET_TOP_NEWS, limit);
        while (rs.next()) {
            newsId.add(rs.getString("newsId"));
        }

        // check posts liked by user
        SqlRowSet rs1 = template.queryForRowSet(SQL_CHECK_POSTS_LIKED_BY_USER, username);
        while (rs1.next()) {
            postsLiked.add(rs1.getString("newsId"));
        }

        for (String id: newsId) {
            SqlRowSet result = template.queryForRowSet(SQL_GET_USER_NEWS, id);
            if (result.next()) {
                News news = News.create(result);
                if (postsLiked.contains(news.getNewsId())) {
                    news.setLiked(true);
                }
                    
                
                SqlRowSet likes = template.queryForRowSet(SQL_GET_LIKES, id);
                if (likes.next()) {
                    news.setLikes(likes.getInt("likes"));
                }
                results.add(news);
            }
        }
        return results;

        
    }
}
