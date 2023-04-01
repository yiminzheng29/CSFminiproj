package sg.edu.nus.pafworkshop21.workshop21.models;

import org.joda.time.DateTime;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Comment {
    
    private String commentId = "";
    private String username;
    private String firstname;
    private String lastname;
    private String newsId = "";
    private String comment;
    private DateTime publishedAt;
    
    public String getCommentId() {return commentId;}
    public void setCommentId(String commentId) {this.commentId = commentId;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getFirstname() {return firstname;}
    public void setFirstname(String firstname) {this.firstname = firstname;}

    public String getLastname() {return lastname;}
    public void setLastname(String lastname) {this.lastname = lastname;}
    
    public String getNewsId() {return newsId;}
    public void setNewsId(String newsId) {this.newsId = newsId;}

    public String getComment() {return comment;}
    public void setComment(String comment) {this.comment = comment;}

    public DateTime getPublishedAt() {return publishedAt;}
    public void setPublishedAt(DateTime publishedAt) {this.publishedAt = publishedAt;}

    public static Comment create(SqlRowSet rs) {
        Comment c = new Comment();
        c.setCommentId(rs.getString("commentId"));
        c.setComment(rs.getString("comment"));
        c.setFirstname(rs.getString("firstname"));
        c.setLastname(rs.getString("lastname"));
        c.setNewsId(rs.getString("newsId"));
        c.setUsername(rs.getString("username"));
        c.setPublishedAt(DateTime.parse(rs.getString("publishedAt")));

        return c;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("commentId", commentId)
            .add("firstname", firstname)
            .add("lastname", lastname)
            .add("newsId", newsId)
            .add("username", username)
            .add("comment", comment)
            .add("publishedAt", publishedAt.toString())
            .build();
    }

    public static Comment create(JsonObject jo) {
        Comment c = new Comment();
        c.setUsername(jo.getString("username"));
        c.setCommentId(jo.getString("commentId"));
        c.setFirstname(jo.getString("firstname"));
        c.setLastname(jo.getString("lastname"));
        c.setPublishedAt(DateTime.parse(jo.getString("publishedAt")));
        c.setNewsId(jo.getString("newsId"));
        c.setComment(jo.getString("comment"));
        return c;
    }
    
}
