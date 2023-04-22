package csf.miniproject.server.csfminiproject.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;

import com.google.firebase.messaging.FirebaseMessagingException;

import csf.miniproject.server.csfminiproject.models.FirebaseNotification;
import csf.miniproject.server.csfminiproject.services.FirebaseService;

@Controller
@RequestMapping(path="/api")
@CrossOrigin(origins = "*")
public class FirebaseController {
    
    @Autowired
    private FirebaseService firebaseSvc;


    @PostMapping(path = "/news/sendNews",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> sendNotification(@RequestPart String recipient, @RequestPart String sender, @RequestPart String message, @RequestPart String urlImage) throws FirebaseMessagingException  {
        FirebaseNotification fNotification = new FirebaseNotification();
        fNotification.setTitle(message);
        fNotification.setMessage("From: " + sender);
        fNotification.setUrlImage(urlImage);
        Optional<String> token = firebaseSvc.getToken(recipient);

        if (token.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }else{
            fNotification.setTarget(token.get());
            firebaseSvc.sendNotification(fNotification);
            return ResponseEntity.ok("ok");
        }
    }

    @PostMapping(path = "/addAFriend",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addFriend(@RequestPart String recipient, @RequestPart String sender, @RequestPart String message) throws FirebaseMessagingException  {


        FirebaseNotification fNotification = new FirebaseNotification();
        fNotification.setTitle("Friend Request");
        fNotification.setMessage(message);
        Optional<String> token = firebaseSvc.getToken(recipient);

        if (token.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }else{
            fNotification.setTarget(token.get());
            firebaseSvc.sendNotification(fNotification);
            return ResponseEntity.ok("ok");
        }
    }

    @PostMapping(path="/saveToken")
    @ResponseBody
    public ResponseEntity<String> saveToken(@RequestParam String username, @RequestParam String token) {
        firebaseSvc.insertToken(token, username);

        return ResponseEntity.ok("Token saved for user %s:%s".formatted(username, token));
    }



    


}
