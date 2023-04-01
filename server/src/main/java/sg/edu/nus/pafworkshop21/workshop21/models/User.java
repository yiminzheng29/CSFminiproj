package sg.edu.nus.pafworkshop21.workshop21.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class User {
    
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Date dob;

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

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public Date getDob() {return dob;}
    public void setDob(Date dob) {this.dob = dob;}

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("username", username)
            .add("password", password)
            .add("firstname", firstname)
            .add("lastname", lastname)
            .add("dob", dob.toString())
            .add("email", email)
            .add("phone", phone)
            .build();
    }

    public static User create(JsonObject jo) throws ParseException{
        User user = new User();
        user.setUsername(jo.getString("username"));
        user.setPassword(jo.getString("password"));
        user.setFirstname(jo.getString("firstname"));
        user.setLastname(jo.getString("lastname"));
        // retrieve DOB as string and parse as date
        String dateOfBirth = jo.getString("dob");
        System.out.println(dateOfBirth);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date dob = sdf.parse(dateOfBirth); 
        user.setDob(dob);
        user.setEmail(jo.getString("email"));
        user.setPhone(jo.getString("phone"));

        return user;
    }

    public static User create(SqlRowSet rs) {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setDob(rs.getDate("dob"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        

        return user;
        
    }
    


}
