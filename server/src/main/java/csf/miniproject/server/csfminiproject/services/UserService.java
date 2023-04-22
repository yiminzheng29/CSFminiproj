package csf.miniproject.server.csfminiproject.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import csf.miniproject.server.csfminiproject.models.User;
import csf.miniproject.server.csfminiproject.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;

    public Optional<User> authenticate (String payload) {
        return userRepo.authenticate(payload);
    }

    public Optional<User> getUser (String username) {
        return userRepo.getUserDetails(username);
    }

    public void deleteUser(String username) {
        userRepo.deleteUser(username);
    }

    public void addFriends(String username, String friendsUsername) {
        userRepo.addFriends(username, friendsUsername);
    }

    public List<User> getFriends(String username) {
        return userRepo.getFriends(username);
    }
    
    public List<User> searchForFriends(String username) throws SQLException{
        return userRepo.searchForUsers(username);
    }

    public void deleteFriend(String username, String friendsUsername) {
        userRepo.deleteFriend(username, friendsUsername);
    }

    public List<User> getNonFriends(String username) throws SQLException{
        return userRepo.getNonFriends(username);
    }
}
