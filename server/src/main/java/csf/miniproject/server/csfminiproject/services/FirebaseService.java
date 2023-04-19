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

    public void insertToken(String token, String username){
        firebaseRepo.insertToken(username, token);
    }

    public Optional<String> getToken(String username){
        return firebaseRepo.getToken(username);
    }
}

// @Service
// public class FirebaseService {

//     private final FirebaseMessaging firebaseMessaging;

//     public FirebaseService(FirebaseMessaging firebaseMessaging) {
//         this.firebaseMessaging = firebaseMessaging;
//     }


//     public void sendNotification(String title, String body, String token) throws FirebaseMessagingException {

//         Notification notification = Notification
//                 .builder()
//                 .setTitle(title)
//                 .setBody(body)
//                 .build();

//         Message message = Message
//                 .builder()
//                 .setToken(token)
//                 .setNotification(notification)
// //              .putAllData(note.getData())
//                 .build();
//         firebaseMessaging.send(message);

// For Send to multiple devices use Multicast Message Builder

//         MulticastMessage multiMessage = MulticastMessage
//                 .builder()
//                 .addAllTokens()
//                 .setNotification(notification)
// //              .putAllData(note.getData())
//                 .build();
//         firebaseMessaging.send(multiMessage);
//     }
