package sg.edu.nus.pafworkshop21.workshop21.models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class IssueDetails {
    
    private String issueNo;
    private String title;
    private String description;
    private String priority;
    private DateTime timeStamp;
    private String status;
    private String user;

    public String getIssueNo() {return issueNo;}
    public void setIssueNo(String issueNo) {this.issueNo = issueNo;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    
    public String getPriority() {return priority;}
    public void setPriority(String priority) {this.priority = priority;}

    public DateTime getTimeStamp() {return timeStamp;}
    public void setTimeStamp(DateTime timeStamp) {this.timeStamp = timeStamp;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    
    public String getUser() {return user;}
    public void setUser(String user) {this.user = user;}

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("issueNo", issueNo)
            .add("title", title)
            .add("description", description)
            .add("priority", priority)
            .add("timestamp",timeStamp.toString())
            .add("status", status)
            .add("username", user)
            .build();
    }

    public static IssueDetails create(SqlRowSet rs) {
        IssueDetails issue = new IssueDetails();
        issue.setIssueNo(rs.getString("issueNo"));
        issue.setTitle(rs.getString("title"));
        issue.setDescription(rs.getString("description"));
        issue.setPriority(rs.getString("priority"));
        issue.setTimeStamp(DateTimeFormat.forPattern("dd/MM/yyyy")
        .parseDateTime(rs.getString("timeStamp")));
        issue.setStatus(rs.getString("status"));
        issue.setStatus(rs.getString("username"));
        // to set user?
        
        return issue;
    }
    
    
}
