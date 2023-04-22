package csf.miniproject.server.csfminiproject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

import csf.miniproject.server.csfminiproject.models.FirebaseNotification;
import csf.miniproject.server.csfminiproject.repositories.FirebaseRepository;

@Service
public class FirebaseService {

    @Autowired
    private FirebaseRepository firebaseRepo;
    
    private final FirebaseMessaging firebaseMessaging;

    public FirebaseService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public String sendNotification(FirebaseNotification notification) throws FirebaseMessagingException {
       
        Message message = Message.builder()
                        .setWebpushConfig(
                                WebpushConfig.builder()
                                        .setNotification(
                                                WebpushNotification.builder()
                                                        .setTitle(notification.getTitle())
                                                        .setImage(notification.getUrlImage())
                                                        .setBody(notification.getMessage())
                                                        .build()
                                        ).build()
                        )
                        .setToken(notification.getTarget())
                        .build();
        return firebaseMessaging.send(message);
    }

    public String addFriendNotification(FirebaseNotification notification) throws FirebaseMessagingException {
        Message message = Message.builder()
                        .setWebpushConfig(
                                WebpushConfig.builder()
                                        .setNotification(
                                                WebpushNotification.builder()
                                                        .setTitle(notification.getTitle())
                                                        .setBody(notification.getMessage())
                                                        .build()
                                        ).build()
                        )
                        .setToken(notification.getTarget())
                        .build();
        return firebaseMessaging.send(message);
    }

    public void insertToken(String token, String username){
        firebaseRepo.insertToken(username, token);
    }

    public Optional<String> getToken(String username){
        return firebaseRepo.getToken(username);
    }
}
