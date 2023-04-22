package csf.miniproject.server.csfminiproject.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static csf.miniproject.server.csfminiproject.repositories.Queries.*;

@Repository
public class FirebaseRepository {
    
    @Autowired 
    private JdbcTemplate jdbctemplate;

    public boolean insertToken(String token, String username){
        int count = jdbctemplate.update(SQL_INSERT_TOKEN,
        token, username);
        return count == 1;
    } 

    public Optional<String> getToken(String username){
        SqlRowSet rs = jdbctemplate.queryForRowSet(SQL_GET_TOKEN, username);
        String token = null;
        try{ 
            rs.next();
            token = rs.getString("token");
            return Optional.of(token);
        }catch(Exception e){
            return Optional.empty();
        }
    } 
}
