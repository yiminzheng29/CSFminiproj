package sg.edu.nus.pafworkshop21.workshop21.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.pafworkshop21.workshop21.models.User;
import sg.edu.nus.pafworkshop21.workshop21.repositories.IssuesRepository;
import sg.edu.nus.pafworkshop21.workshop21.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private IssuesRepository issuesRepo;


    public void saveUser(User user) throws Exception{
        userRepo.saveUser(user);
    }

    public Optional<User> authenticate (String payload) {
        return userRepo.authenticate(payload);
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
