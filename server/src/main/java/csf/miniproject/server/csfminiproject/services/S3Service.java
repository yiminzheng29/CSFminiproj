package csf.miniproject.server.csfminiproject.services;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import csf.miniproject.server.csfminiproject.models.User;
import csf.miniproject.server.csfminiproject.repositories.UserRepository;

@Service
public class S3Service {
    private Logger logger = Logger.getLogger(S3Service.class.getName());

    
    @Value("${spaces.bucket}")
	private String spacesBucket;

	@Value("${spaces.endpoint.url}")
	private String spacesEndpointUrl;

    @Autowired
    private AmazonS3 s3;

    @Autowired
    private UserRepository userRepo;


    public User upload(User user, MultipartFile file) throws Exception{

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            PutObjectRequest putReq = new PutObjectRequest(spacesBucket, user.getUsername(), file.getInputStream(), metadata);
            putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putReq);
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, "Put S3", ex);
            return null;
        }
        String imageUrl = "https://%s.%s/%s".formatted(spacesBucket, spacesEndpointUrl, user.getUsername());
        System.out.println(imageUrl);
        user.setProfileImageUrl(imageUrl);
        userRepo.saveUser(file, user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstname(), user.getLastname(), imageUrl);
        
        return user;
    }

    public User update(User user, MultipartFile file) throws Exception{

        Map<String, String> userData = new HashMap<>();
        userData.put("username", user.getUsername());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            PutObjectRequest putReq = new PutObjectRequest(spacesBucket, user.getUsername(), file.getInputStream(), metadata);
            putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putReq);
        }
        catch (Exception ex) {
            return null;
        }
        String imageUrl = "https://%s.%s/%s".formatted(spacesBucket, spacesEndpointUrl, user.getUsername());

        user.setProfileImageUrl(imageUrl);
        userRepo.updateUser(user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getEmail(), file, imageUrl);
        
        return user;
    }

}
