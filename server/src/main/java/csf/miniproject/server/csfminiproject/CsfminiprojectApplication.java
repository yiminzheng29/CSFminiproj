package csf.miniproject.server.csfminiproject;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import csf.miniproject.server.csfminiproject.services.TelegramBot;

@SpringBootApplication
public class CsfminiprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsfminiprojectApplication.class, args);
		try {
            TelegramBotsApi tgApi = new TelegramBotsApi(DefaultBotSession.class);
            tgApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}

	@Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("firebase_config.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = null;
        if(FirebaseApp.getApps().isEmpty()) {
            app = FirebaseApp.initializeApp(firebaseOptions, "newsApp");
        }else {
            app = FirebaseApp.initializeApp(firebaseOptions);
        }
        
        return FirebaseMessaging.getInstance(app);
    }
}
