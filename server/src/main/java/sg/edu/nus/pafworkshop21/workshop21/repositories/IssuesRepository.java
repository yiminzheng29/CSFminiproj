package sg.edu.nus.pafworkshop21.workshop21.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.pafworkshop21.workshop21.models.IssueDetails;
import static sg.edu.nus.pafworkshop21.workshop21.repositories.Queries.*;


@Repository
public class IssuesRepository {
    
    @Autowired
    private JdbcTemplate template;

    public void saveIssue(IssueDetails issue) {
        template.update(SQL_SAVE_ISSUE, 
            issue.getIssueNo(), 
            issue.getTitle(),
            issue.getDescription(),
            issue.getPriority(),
            issue.getStatus(),
            issue.getUser(), 
            issue.getTimeStamp());
    }
}
