package csf.miniproject.server.csfminiproject.controllers;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.gmail.Gmail.Users;
import com.google.protobuf.Method;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import csf.miniproject.server.csfminiproject.models.News;
import csf.miniproject.server.csfminiproject.models.User;
import csf.miniproject.server.csfminiproject.services.GmailService;
import csf.miniproject.server.csfminiproject.services.NewsService;
import csf.miniproject.server.csfminiproject.services.S3Service;
import csf.miniproject.server.csfminiproject.services.UserService;

@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userSvc;

    @Autowired
    private NewsService newsSvc;

    @Autowired
    private S3Service s3Svc;

    @Autowired
    private GmailService mailSvc;

    @PostMapping(path="/createUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void createUser(@RequestPart MultipartFile profileImage, @RequestPart String username, @RequestPart String password,
        @RequestPart String firstname, @RequestPart String lastname, @RequestPart String email) throws Exception{

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setProfileImage(profileImage.getBytes());
        
        mailSvc.sendMail(user);
        s3Svc.upload(user, profileImage);
        
        
    }

    @PostMapping(path="/user")
    @ResponseBody
    public ResponseEntity<User> login(@RequestBody String payload){


        Optional<User> opt = userSvc.authenticate(payload);

        return ResponseEntity.ok(opt.get());
    }

    @GetMapping(path="/user/{username}")
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable String username) {
        Optional<User> opt = userSvc.getUser(username);

        if (opt.isEmpty()) {
            return null;
        }

        return ResponseEntity.ok(opt.get());
    }


    @PutMapping(path="/user/{username}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateUser(@PathVariable String username, 
        @RequestPart MultipartFile profileImage, @RequestPart String password, @RequestPart String email, @RequestPart String firstname, @RequestPart String lastname) throws Exception{

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setProfileImage(profileImage.getBytes());

        mailSvc.sendUpdateMail(user);
        s3Svc.update(user, profileImage);

    }

    @DeleteMapping(path="/user/{username}/delete")
    @ResponseBody
    public void deleteUser(@PathVariable String username) throws Exception{
    
        Optional<User> opt = userSvc.getUser(username);
        if (opt.isPresent())    {
            mailSvc.sendDeleteMail(opt.get());
        }
        userSvc.deleteUser(username);
    }

    @PostMapping(path="/like", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> likeNews(@RequestPart String username, @RequestPart String title, 
        @RequestPart String publishedAt, @RequestPart(required = false) String author, @RequestPart(required = false) String description, 
        @RequestPart String sourceName, @RequestPart(required = false) String url, @RequestPart(required = false) String urlImage, 
        @RequestPart(required = false) String content){

            // 2023-04-21T01:19:37.000Z //"2023-04-22T15:17:18.716+08:00"
            if (publishedAt.contains("+")) {
                publishedAt = publishedAt.substring(0, publishedAt.length()-6);
            }
            JsonObject jo = Json.createObjectBuilder()
                .add("title", title)
                .add("publishedAt", publishedAt)
                .add("author", author!=null?author:"")
                .add("sourceName", sourceName)
                .add("url", url!=null?url:"")
                .add("urlImage", urlImage!=null?urlImage:"")
                .add("content", content!=null?content:"")
                .add("description", description!=null?description:"")
                .build();

            News news = News.save(jo);
            News result = newsSvc.likeNews(news, username);

            return ResponseEntity.ok(result.toJson().toString());
    }

    @PostMapping(path="/save")
    @ResponseBody
    public ResponseEntity<String> saveNews(@RequestPart String title, 
        @RequestPart String publishedAt, @RequestPart(required = false) String author, @RequestPart(required = false) String description, 
        @RequestPart String sourceName, @RequestPart(required = false) String url, @RequestPart(required = false) String urlImage, @RequestPart(required = false) String content){

            JsonObject jo = Json.createObjectBuilder()
                .add("title", title)
                .add("publishedAt", publishedAt)
                .add("author", author!=null?author:"")
                .add("sourceName", sourceName)
                .add("url", url!=null?url:"")
                .add("urlImage", urlImage!=null?urlImage:"")
                .add("content", content!=null?content:"")
                .add("description", description!=null?description:"")
                .build();

            News news = News.save(jo);
            News result = newsSvc.saveNews(news);

            return ResponseEntity.ok(result.toJson().toString());
    }

    @GetMapping(path="/news/authors")
    @ResponseBody
    public ResponseEntity<List<String>> getAllCategories(@RequestParam String country, @RequestParam String username) throws Exception {

        List<News> result = newsSvc.getNews(country, username);
        List<String> resultSources = new LinkedList<>();
        
        // for getting sources only
        result.stream().forEach(x -> {
            resultSources.add(x.getSourceName());
        });
        return ResponseEntity.ok(resultSources);
    }

    @GetMapping(path="/news/allNews")
    @ResponseBody
    public ResponseEntity<String> getAllNews(@RequestParam String country, @RequestParam String username) throws Exception {

        List<News> result = newsSvc.getNews(country, username);
        
        // for getting all the news only
        JsonArrayBuilder jArr = Json.createArrayBuilder();
        result.stream().forEach(x -> 
            jArr.add(x.toJson()));

        return ResponseEntity.ok(jArr.build().toString());
    }

    // e.g. http://localhost:8080/api/dislike/c27619?username=kwr
    @DeleteMapping(path="/dislike/{newsId}")
    @ResponseBody
    public ResponseEntity<String> deleteNews(@RequestParam String username, @PathVariable String newsId) {

        newsSvc.unlikeNews(newsId, username);
        
        return ResponseEntity.ok(username);
    }

    // get all news liked by user
    @GetMapping(path="/news/{username}")
    @ResponseBody
    public ResponseEntity<String> getAllNewsByUser(@PathVariable String username) {

        List<News> allNews = newsSvc.getNewsByUser(username);

        JsonArrayBuilder jArr = Json.createArrayBuilder();
        allNews.stream().forEach(x -> 
            jArr.add(x.toJson()));

        return ResponseEntity.ok(jArr.build().toString());
    }

    @PostMapping(path="/addFriends/{username}")
    @ResponseBody
    public void addFriend(@PathVariable String username, @RequestParam String friendsUsername) {
        userSvc.addFriends(username, friendsUsername);
    }

    @GetMapping(path="/friends/{username}")
    @ResponseBody
    public ResponseEntity<String> getAllFriends(@PathVariable String username) {
        List<User> users = userSvc.getFriends(username);
        JsonArrayBuilder jArr = Json.createArrayBuilder();
        users.stream().forEach(x -> 
            jArr.add(x.toJson()));

        return ResponseEntity.ok(jArr.build().toString());
    }

    @GetMapping(path="/searchFriends/{username}")
    @ResponseBody
    public ResponseEntity<String> searchFriends(@PathVariable String username, @RequestParam String keyword) throws SQLException{
        List<User> users = userSvc.searchForFriends(keyword);
        JsonArrayBuilder jArr = Json.createArrayBuilder();
        users.stream().forEach(x -> 
            jArr.add(x.toJson()));

        return ResponseEntity.ok(jArr.build().toString());
    }

    @DeleteMapping(path="/deleteFriend/{username}")
    @ResponseBody
    public void deleteFriend(@PathVariable String username, @RequestParam String friendsUsername) {
        userSvc.deleteFriend(username, friendsUsername);
    }

    @GetMapping(path="/nonFriends/{username}")
    @ResponseBody
    public ResponseEntity<String> getNonFriends(@PathVariable String username) throws SQLException {
        List<User> users = userSvc.getNonFriends(username);
        JsonArrayBuilder jArr = Json.createArrayBuilder();
        users.stream().forEach(x -> 
            jArr.add(x.toJson()));

        return ResponseEntity.ok(jArr.build().toString());
    }
}
