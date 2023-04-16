package csf.miniproject.server.csfminiproject.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import csf.miniproject.server.csfminiproject.models.User;
import csf.miniproject.server.csfminiproject.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;


    // public void saveUser(MultipartFile file, String username, String password, String email, String firstname, String lastname, String imageUrl) throws Exception{
    //     userRepo.saveUser(file, username, password, email, firstname, lastname, imageUrl);
    // }

    public Optional<User> authenticate (String payload) {
        return userRepo.authenticate(payload);
    }

    public Optional<User> getUser (String username) {
        return userRepo.getUserDetails(username);
    }


    // public void updateUser(MultipartFile file, String username, String password, String email, String firstname, String lastname) throws Exception {
    //     userRepo.updateUser(file, username, password, firstname, lastname, email);
    // }

    public void deleteUser(String username) {
        userRepo.deleteUser(username);
    }

    // public String saveIssue(JsonObject payload, User user) {
        
    //     String issueNo = "";

    //     if (authenticate(user)) {
    //         IssueDetails id = new IssueDetails();
    //         issueNo = UUID.randomUUID().toString().substring(0,8);
    //         id.setTitle(payload.getString("title"));
    //         id.setIssueNo(issueNo);
    //         id.setPriority(payload.getString("priority"));
    //         id.setDescription(payload.getString("description"));
    //         id.setStatus(payload.getString("status"));
    //         id.setTimeStamp(new DateTime(
    //             DateTimeFormat.forPattern("dd/MM/yyyy")
    //                     .parseDateTime(payload.getString("timeStamp"))));
    //         id.setUser(user.getUsername());

    //         issuesRepo.saveIssue(id);
            
    //     }
    //     return issueNo;
        
    // }
    

}
