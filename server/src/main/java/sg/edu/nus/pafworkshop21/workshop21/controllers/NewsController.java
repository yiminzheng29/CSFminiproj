package sg.edu.nus.pafworkshop21.workshop21.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import sg.edu.nus.pafworkshop21.workshop21.models.News;
import sg.edu.nus.pafworkshop21.workshop21.services.NewsService;

@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class NewsController {

    @Autowired
    private NewsService newsSvc;

    @GetMapping(path="/news/{searchQuery}/results")
    @ResponseBody
    public ResponseEntity<String> getNewsResults(@PathVariable String searchQuery, @RequestParam String username) {

        List<News> results = newsSvc.searchNews(searchQuery, username);

        // for converting news to json
        JsonArrayBuilder jArr = Json.createArrayBuilder();
        results.stream().forEach(x -> 
                    jArr.add(x.toJson()));

        return ResponseEntity.ok(jArr.build().toString());
    }
    
}
