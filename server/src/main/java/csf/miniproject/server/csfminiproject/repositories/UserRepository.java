package csf.miniproject.server.csfminiproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
import java.util.Optional;

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

        // template.update(SQL_CREATE_USER, user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getDob(), user.getEmail(), user.getPhone());
    }

    // for authenticating user
    public Optional<User> authenticate(String payload) {
        StringReader reader = new StringReader(payload);
        JsonReader jr = Json.createReader(reader);
        JsonObject jo = jr.readObject();
        
        String username = jo.getString("username");
        String password = jo.getString("password");
        // final SqlRowSet rs = template.queryForRowSet(SQL_GET_USER, username, password);

        // if (!rs.next())
        //     return Optional.empty();

        // return Optional.of(User.create(rs));

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
        
        // User user = new User();
        // SqlRowSet rs = template.queryForRowSet(SQL_SEARCH_USER, username);
        // if (rs.next()) {
        //     user = User.create(rs);
        // }
        // return user;
        return template.query(SQL_SEARCH_USER, (ResultSet rs) -> {
            if (!rs.next()) {
                return Optional.empty();
            }
            User user = User.create(rs);
            return Optional.of(user);
        }, username);
    }

    // public void updateUser(User user) {
    //     template.update(SQL_UPDATE_USER, user.getPassword(), user.getFirstname(), 
    //         user.getLastname(), user.getDob(), user.getEmail(), user.getPhone(), user.getUsername());
    // }

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

        // template.update(SQL_CREATE_USER, user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getDob(), user.getEmail(), user.getPhone());
    }

    public void deleteUser(String username) {
        template.update(SQL_DELETE_USER, username);
    }

    
}