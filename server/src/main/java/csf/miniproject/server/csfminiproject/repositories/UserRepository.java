package csf.miniproject.server.csfminiproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import csf.miniproject.server.csfminiproject.models.User;
import static csf.miniproject.server.csfminiproject.repositories.Queries.*;

import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate template;

    @Autowired
    private DataSource ds;

    // save user details in database
    public void saveUser(MultipartFile file, String username, String password, String email, String firstname, String lastname, String profileImageUrl) throws Exception{
        try (Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_CREATE_USER)) {
                InputStream is = file.getInputStream();
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, firstname);
                ps.setString(4, lastname);
                ps.setString(5, email);
                ps.setBinaryStream(6, is, file.getSize());
                ps.setString(7, profileImageUrl);
                ps.executeUpdate();

            }

    }

    // for authenticating user
    public Optional<User> authenticate(String payload) {
        StringReader reader = new StringReader(payload);
        JsonReader jr = Json.createReader(reader);
        JsonObject jo = jr.readObject();
        
        String username = jo.getString("username");
        String password = jo.getString("password");

        return template.query(SQL_GET_USER, 
            (ResultSet rs) -> {
                if (!rs.next())
                    return Optional.empty();
            final User user = User.create(rs);
            return Optional.of(user);
        }, username, password);
    }

    // get user details in the user profile page
    public Optional<User> getUserDetails(String username) {
        
        return template.query(SQL_SEARCH_USER, (ResultSet rs) -> {
            if (!rs.next()) {
                return Optional.empty();
            }
            User user = User.create(rs);
            return Optional.of(user);
        }, username);
    }

    public void updateUser(String username, String password, String firstname, String lastname, String email, MultipartFile file, String profileImageUrl) throws Exception{
        try (Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_UPDATE_USER)) {
                InputStream is = file.getInputStream();
                ps.setString(7, username);
                ps.setString(1, password);
                ps.setString(2, firstname);
                ps.setString(3, lastname);
                ps.setString(4, email);
                ps.setBinaryStream(5, is, file.getSize());
                ps.setString(6, profileImageUrl);
                
                ps.executeUpdate();

            }

    }

    public void deleteUser(String username) {
        template.update(SQL_DELETE_USER, username);
    }

    public void addFriends(String username, String friendsUsername) {
        template.update(SQL_ADD_FRIENDS, username, friendsUsername);
        template.update(SQL_ADD_FRIENDS, friendsUsername, username);
    }

    public List<User> getFriends(String username) {
        List<String> friends = new LinkedList<>();
        List<User> results = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_GET_FRIENDS, username);
        while (rs.next()) 
            friends.add(rs.getString("friends_username"));

        for (String f: friends) {
            template.query(SQL_RETRIEVE_FRIEND_RECORD, (ResultSet r) -> {

                User u = User.create(r);
                results.add(u);
            }, f);}
        return results;
    }

    public List<User> searchForUsers(String keyword) throws SQLException{
        List<User> results = new LinkedList<>();
        String query = "%".concat(keyword).concat("%");

        SqlRowSet rs = template.queryForRowSet(SQL_GET_LIST_OF_USERS, query);
        while (rs.next()) 
            results.add(User.rowSetCreate(rs));

        return results;
    }
    
    public void deleteFriend(String username, String friendsUsername) {
        template.update(SQL_DELETE_FRIEND, username, friendsUsername);
        template.update(SQL_DELETE_FRIEND, friendsUsername, username);
    }

    public List<User> getNonFriends(String username) throws SQLException{
        List<String> friends = new LinkedList<>();
        List<User> results = new LinkedList<>();
        List<String> allUsers = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_GET_FRIENDS, username);
        while (rs.next()) 
            friends.add(rs.getString("friends_username"));

        SqlRowSet rs1 = template.queryForRowSet(SQL_GET_ALL_USERS);
            while (rs1.next()) 
                allUsers.add(rs1.getString("username"));

        allUsers.removeAll(friends);
        allUsers.remove(username);
        Integer limit = Integer.min(10, allUsers.size());
        allUsers = allUsers.subList(0, limit);

        for (String f: allUsers) {
            template.query(SQL_RETRIEVE_FRIEND_RECORD, (ResultSet r) -> {

                User u = User.create(r);
                results.add(u);
            }, f);}

        
        return results;
    }
}
