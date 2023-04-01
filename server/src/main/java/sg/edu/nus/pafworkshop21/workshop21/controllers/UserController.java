package sg.edu.nus.pafworkshop21.workshop21.controllers;

import java.io.StringReader;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.pafworkshop21.workshop21.models.Comment;

import sg.edu.nus.pafworkshop21.workshop21.models.News;
import sg.edu.nus.pafworkshop21.workshop21.models.User;
import sg.edu.nus.pafworkshop21.workshop21.services.CommentService;
import sg.edu.nus.pafworkshop21.workshop21.services.NewsService;
import sg.edu.nus.pafworkshop21.workshop21.services.UserService;

@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userSvc;

    @Autowired
    private NewsService newsSvc;

    @Autowired
    private CommentService cSvc;


    @PostMapping(path="/createUser")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody String payload) throws Exception{
        System.out.println(payload);
        
        JsonReader jr = Json.createReader(new StringReader(payload));
        JsonObject jo = jr.readObject();

        User user = User.create(jo);

        userSvc.saveUser(user);

        return ResponseEntity.ok().body(jo.toString());
    }

    @PostMapping(path="/user")
    @ResponseBody
    public ResponseEntity<User> login(@RequestBody String payload){

        System.out.println(payload);

        Optional<User> opt = userSvc.authenticate(payload);

        return ResponseEntity.ok(opt.get());
    }

    @PostMapping(path="/like", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> likeNews(@RequestPart String username, @RequestPart String title, 
        @RequestPart String publishedAt, @RequestPart(required = false) String author, @RequestPart(required = false) String description, 
        @RequestPart String sourceName, @RequestPart(required = false) String url, @RequestPart(required = false) String urlImage, 
        @RequestPart(required = false) String content){

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

            System.out.println(result.toJson().toString());
            return ResponseEntity.ok(result.toJson().toString());
    }

    @PostMapping(path="/save", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    // @PostMapping(path="/{username}/createIssue")
    // @ResponseBody
    // public ResponseEntity<String> createIssue(@RequestBody String payload, @PathVariable User user) {

    //     System.out.println(payload);

    //     JsonReader jr = Json.createReader(new StringReader(payload));
    //     JsonObject jo = jr.readObject();
        
    //     IssueDetails id = new IssueDetails();
        
    // }

    // displays all the news
    @GetMapping(path="/news/authors")
    @ResponseBody
    public ResponseEntity<List<String>> getAllCategories(@RequestParam String country, @RequestParam String username) {

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
    public ResponseEntity<String> getAllNews(@RequestParam String country, @RequestParam String username) {

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

    // postComment
    @PostMapping(path="/postComment", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> postComment(@RequestPart String firstname, @RequestPart String lastname, 
        @RequestPart String username, @RequestPart String comment, @RequestPart String newsId) {
            
            System.out.println(comment);
            Comment c = new Comment();
            c.setComment(comment);
            c.setUsername(username);
            c.setFirstname(firstname);
            c.setLastname(lastname);
            c.setNewsId(newsId);
            c.setPublishedAt(DateTime.now());
            
            cSvc.saveComment(c);
            System.out.println(c.getNewsId());

            return ResponseEntity.ok(c.toJson().toString());
        }

    @GetMapping(path="/getComments")
    @ResponseBody
    public ResponseEntity<String> getComments() {

        List<Comment> comments = cSvc.getComments();
        System.out.println(comments.size());

        
        JsonArrayBuilder arr = Json.createArrayBuilder();
        for(Comment c: comments)
            arr.add(c.toJson());

        return ResponseEntity.ok(arr.build().toString());

    }
}
