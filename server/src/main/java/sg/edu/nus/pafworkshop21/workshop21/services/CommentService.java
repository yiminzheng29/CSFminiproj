package sg.edu.nus.pafworkshop21.workshop21.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.pafworkshop21.workshop21.models.Comment;
import sg.edu.nus.pafworkshop21.workshop21.repositories.CommentRepository;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository cRepo;

    public void saveComment(Comment c) {
        cRepo.saveComment(c);
    }

    public List<Comment> getComments() {
        return cRepo.getComments();
    }
}
