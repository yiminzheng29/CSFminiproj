package csf.miniproject.server.csfminiproject.models;

import java.sql.SQLException;
import java.text.ParseException;

import java.sql.ResultSet;

import org.springframework.jdbc.support.rowset.SqlRowSet;


import jakarta.json.Json;
import jakarta.json.JsonObject;

public class User {
    
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private byte[] profileImage;
    private String profileImageUrl;

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    
    public String getFirstname() {return firstname;}
    public void setFirstname(String firstname) {this.firstname = firstname;}

    public String getLastname() {return lastname;}
    public void setLastname(String lastname) {this.lastname = lastname;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public byte[] getProfileImage() {return profileImage;}
    public void setProfileImage(byte[] profileImage) {this.profileImage = profileImage;}

    public String getProfileImageUrl() {return profileImageUrl;}
    public void setProfileImageUrl(String profileImageUrl) {this.profileImageUrl = profileImageUrl;}

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("username", username)
            .add("password", password)
            .add("firstname", firstname)
            .add("lastname", lastname)
            .add("email", email)
            .add("profileImage", profileImageUrl)
            .build();
    }

    public static User create(JsonObject jo) throws ParseException{
        User user = new User();
        user.setUsername(jo.getString("username"));
        user.setPassword(jo.getString("password"));
        user.setFirstname(jo.getString("firstname"));
        user.setLastname(jo.getString("lastname"));
        user.setEmail(jo.getString("email"));
        user.setProfileImageUrl(jo.getString("profileImage"));

        return user;
    }

    public static User create(ResultSet rs) throws SQLException{
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        user.setProfileImage(rs.getBytes("profileImage"));
        user.setProfileImageUrl(rs.getString("profileImageUrl"));
        

        return user;
    }

    public static User rowSetCreate(SqlRowSet rs) throws SQLException{
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        user.setProfileImageUrl(rs.getString("profileImageUrl"));
        

        return user;
    }


}
