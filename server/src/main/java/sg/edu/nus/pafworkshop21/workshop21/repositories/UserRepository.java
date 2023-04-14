package sg.edu.nus.pafworkshop21.workshop21.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.pafworkshop21.workshop21.models.User;
import static sg.edu.nus.pafworkshop21.workshop21.repositories.Queries.*;
import java.io.StringReader;
import java.util.Optional;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate template;

    // save user details in database
    public void saveUser(User user) throws Exception{
        template.update(SQL_CREATE_USER, user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getDob(), user.getEmail(), user.getPhone());
    }

    // for authenticating user
    public Optional<User> authenticate(String payload) {
        StringReader reader = new StringReader(payload);
        JsonReader jr = Json.createReader(reader);
        JsonObject jo = jr.readObject();
        
        String username = jo.getString("username");
        String password = jo.getString("password");
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_USER, username, password);

        if (!rs.next())
            return Optional.empty();

        return Optional.of(User.create(rs));
    }

    // get user details in the user profile page
    public User getUserDetails(String username) {
        
        User user = new User();
        SqlRowSet rs = template.queryForRowSet(SQL_SEARCH_USER, username);
        if (rs.next()) {
            user = User.create(rs);
        }
        return user;
    }

    public void updateUser(User user) {
        template.update(SQL_UPDATE_USER, user.getPassword(), user.getFirstname(), 
            user.getLastname(), user.getDob(), user.getEmail(), user.getPhone(), user.getUsername());
    }

    public void deleteUser(String username) {
        template.update(SQL_DELETE_USER, username);
    }

    
}
